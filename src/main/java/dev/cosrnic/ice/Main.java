package dev.cosrnic.ice;

import dev.cosrnic.ice.rules.AnvilRule;
import dev.cosrnic.ice.rules.ButtonRule;
import dev.cosrnic.ice.rules.FenceRule;
import dev.cosrnic.ice.rules.StairsRule;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

public class Main {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        DimensionType FULLBRIGHT = DimensionType.builder(NamespaceID.from("placement:fullbright")).ambientLight(2f).build();
        MinecraftServer.getDimensionTypeManager().addDimension(FULLBRIGHT);

        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(FULLBRIGHT);
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(-64, 0, Block.GRASS_BLOCK);
        });

        {
            GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
            eventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
                Player player = event.getPlayer();

                event.setSpawningInstance(instanceContainer);
                player.setRespawnPoint(new Pos(0, 1, 0));
                player.setGameMode(GameMode.CREATIVE);
                player.setPermissionLevel(4);
            });
        }

        {
            BlockManager blockManager = MinecraftServer.getBlockManager();

            Block.values().forEach(block -> {
                if (block.name().endsWith("_stairs")) {
                    blockManager.registerBlockPlacementRule(new StairsRule(block));
                } else if (block.name().equals("minecraft:anvil") || block.name().endsWith("_anvil")) {
                    blockManager.registerBlockPlacementRule(new AnvilRule(block));
                } else if (block.name().endsWith("_button")) {
                    blockManager.registerBlockPlacementRule(new ButtonRule(block));
                } else if (block.name().endsWith("_fence")) {
                    blockManager.registerBlockPlacementRule(new FenceRule(block));
                }
            });
        }

        MojangAuth.init();

        server.start("localhost", 25565);
    }

}
