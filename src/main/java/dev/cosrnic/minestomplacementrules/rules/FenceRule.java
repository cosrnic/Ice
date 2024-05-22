package dev.cosrnic.minestomplacementrules.rules;

import dev.cosrnic.minestomplacementrules.utils.RuleUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.gamedata.tags.TagManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FenceRule extends BlockPlacementRule {
    public FenceRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @NotNull Block blockUpdate(@NotNull BlockPlacementRule.UpdateState updateState) {
        Block.Getter instance = updateState.instance();
        Point placePos = updateState.blockPosition();
        Point north = placePos.relative(BlockFace.NORTH);
        Point east = placePos.relative(BlockFace.EAST);
        Point south = placePos.relative(BlockFace.SOUTH);
        Point west = placePos.relative(BlockFace.WEST);

        return updateState.currentBlock().withProperties(Map.of(
                RuleUtils.NORTH, String.valueOf(canConnect(instance, north, BlockFace.SOUTH)),
                RuleUtils.EAST, String.valueOf(canConnect(instance, east, BlockFace.WEST)),
                RuleUtils.SOUTH, String.valueOf(canConnect(instance, south, BlockFace.NORTH)),
                RuleUtils.WEST, String.valueOf(canConnect(instance, west, BlockFace.EAST))
        ));
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        Block.Getter instance = placementState.instance();
        Point placePos = placementState.placePosition();
        Point north = placePos.relative(BlockFace.NORTH);
        Point east = placePos.relative(BlockFace.EAST);
        Point south = placePos.relative(BlockFace.SOUTH);
        Point west = placePos.relative(BlockFace.WEST);


        return placementState.block().withProperties(Map.of(
                RuleUtils.NORTH, String.valueOf(canConnect(instance, north, BlockFace.SOUTH)),
                RuleUtils.EAST, String.valueOf(canConnect(instance, east, BlockFace.WEST)),
                RuleUtils.SOUTH, String.valueOf(canConnect(instance, south, BlockFace.NORTH)),
                RuleUtils.WEST, String.valueOf(canConnect(instance, west, BlockFace.EAST))
        ));
    }

    private boolean canConnect(Block.Getter instance, Point pos, BlockFace blockFace) {
        Block instanceBlock = instance.getBlock(pos);
        boolean canConnectToFence = canConnectToFence(instanceBlock);
        boolean canFenceGateConnect = instanceBlock.name().endsWith("_fence_gate") && RuleUtils.getAxis(RuleUtils.getFacing(instanceBlock).toDirection()).equals(RuleUtils.getAxis(blockFace.toDirection()));
        boolean isFaceFull = instanceBlock.registry().collisionShape().isFaceFull(blockFace);


        return !cannotConnect(instanceBlock) && isFaceFull || canConnectToFence || canFenceGateConnect;
    }

    private boolean canConnectToFence(Block block) {
        TagManager tagManager = MinecraftServer.getTagManager();
        System.out.println(tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:fences").getValues());
        System.out.println(tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:wooden_fences").getValues());
        return tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:fences").contains(block.namespace()) && tagManager.getTag(Tag.BasicType.BLOCKS, "minecraft:wooden_fences").contains(block.namespace());
    }

    private boolean cannotConnect(Block block) {
        String name = block.name().replaceAll("minecraft:", "");
        return name.endsWith("leaves")
            ||
            name.equals("barrier")
            ||
            name.equals("carved_pumpkin")
            ||
            name.equals("jack_o_lantern")
            ||
            name.equals("melon")
            ||
            name.equals("pumpkin")
            ||
            name.endsWith("_shulker_box");
    }
}
