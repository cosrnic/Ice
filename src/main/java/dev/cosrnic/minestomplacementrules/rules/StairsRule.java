package dev.cosrnic.minestomplacementrules.rules;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

import static dev.cosrnic.minestomplacementrules.utils.RuleUtils.*;

public class StairsRule extends BlockPlacementRule {

    public StairsRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @NotNull Block blockUpdate(@NotNull BlockPlacementRule.UpdateState updateState) {
        return updateState.currentBlock().withProperty(SHAPE, getShape(updateState.instance(), updateState.currentBlock(), updateState.blockPosition()));
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        BlockFace placementFace = placementState.blockFace();
        Point placementPos = placementState.placePosition();
        Point cursorPos = Objects.requireNonNullElse(placementState.cursorPosition(), Vec.ZERO);
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);

        BlockFace half = placementFace == BlockFace.BOTTOM
                ||
                placementFace != BlockFace.TOP
                &&
                cursorPos.y() > 0.5
                ? BlockFace.TOP
                : BlockFace.BOTTOM;
        BlockFace facing = BlockFace.fromYaw(playerPos.yaw());

        Block block = this.block.withProperties(Map.of(
            HALF, half.name().toLowerCase(),
            FACING, facing.name().toLowerCase()
        ));

        block = block.withProperty(SHAPE, getShape(placementState.instance(), block, placementPos));

        return canPlaceAt(placementState.instance(), placementPos, placementFace.toDirection().opposite(), placementState.isPlayerShifting()) ? block : null;

    }

    private String getShape(Block.Getter instance, Block block, Point blockPos) {
        Direction direction = getFacing(block).toDirection();
        Block offsetBlock = instance.getBlock(blockPos.add(direction.normalX(), direction.normalY(), direction.normalZ()));
        Direction offsetDirection = getFacing(offsetBlock).toDirection();
        Block oppositeOffsetBlock = instance.getBlock(blockPos.add(direction.opposite().normalX(), direction.opposite().normalY(), direction.opposite().normalZ()));
        Direction oppositeOffsetDirection = getFacing(oppositeOffsetBlock).toDirection();

        if (isStairs(offsetBlock)
            &&
            getHalf(block) == getHalf(offsetBlock)
            &&
            getAxis(offsetDirection) != getAxis(direction)
            &&
            isDifferentOrientation(instance, block, blockPos, offsetDirection.opposite())
        ) {
            if (offsetDirection == rotateYCounterclockwise(direction)) {
                return "outer_left";
            } else {
                return "outer_right";
            }
        }

        if (isStairs(oppositeOffsetBlock)
            &&
            getHalf(block) == getHalf(oppositeOffsetBlock)
            &&
            getAxis(oppositeOffsetDirection) != getAxis(direction)
            &&
            isDifferentOrientation(instance, block, blockPos, oppositeOffsetDirection)
        ) {
            if (oppositeOffsetDirection == rotateYCounterclockwise(direction)) {
                return "inner_left";
            } else {
                return "inner_right";
            }
        }

        return "straight";
    }

    private boolean isDifferentOrientation(Block.Getter instance, Block block, Point blockPos, Direction direction) {
        BlockFace facing = getFacing(block);
        BlockFace half = getHalf(block);
        Block instanceBlock = instance.getBlock(blockPos.add(direction.normalX(), direction.normalY(), direction.normalZ()));
        BlockFace instanceBlockFacing = getFacing(instanceBlock);
        BlockFace instanceBlockHalf = getHalf(instanceBlock);

        return !isStairs(instanceBlock) || instanceBlockFacing != facing || instanceBlockHalf != half;
    }

    private boolean isStairs(Block block) {
        return block.name().endsWith("_stairs");
    }


}
