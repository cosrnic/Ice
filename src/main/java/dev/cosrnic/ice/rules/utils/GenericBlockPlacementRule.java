package dev.cosrnic.ice.rules.utils;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GenericBlockPlacementRule extends BlockPlacementRule {
    protected GenericBlockPlacementRule(@NotNull Block block) {
        super(block);
    }

    private final List<String> INTERACTION_BLOCK_NAMES = List.of(
            "_button", // does a check for endsWith or if this list contains the block name
            "minecraft:anvil",
            "_anvil"

    );

    protected boolean canPlaceAt(Block.Getter instance, Point blockPos, Direction direction, boolean shifting) {
        Block offset = instance.getBlock(blockPos.add(direction.normalX(), direction.normalY(), direction.normalZ()));

        if (((INTERACTION_BLOCK_NAMES.contains(offset.name())
                ||
                INTERACTION_BLOCK_NAMES.stream()
                        .filter(s -> offset.name().endsWith(s))
                        .findFirst()
                        .orElse(null)
                        != null)
                && !shifting
        )) return false;
        return offset.registry().collisionShape().isFaceFull(BlockFace.fromDirection(direction).getOppositeFace());
    }
}
