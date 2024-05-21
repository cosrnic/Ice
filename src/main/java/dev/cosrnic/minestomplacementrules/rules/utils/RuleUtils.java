package dev.cosrnic.minestomplacementrules.rules.utils;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;

public class RuleUtils {

    public static final String HALF = "half";
    public static final String FACING = "facing";
    public static final String SHAPE = "shape";
    public static final String WATERLOGGED = "waterlogged";

    public static BlockFace getHalf(Block block) {
        if (block.getProperty(HALF) == null) return BlockFace.BOTTOM;
        return BlockFace.valueOf(block.getProperty(HALF).toUpperCase());
    }

    public static BlockFace getFacing(Block block) {
        if (block.getProperty(FACING) == null) return BlockFace.NORTH;
        return BlockFace.valueOf(block.getProperty(FACING).toUpperCase());
    }

    public static Direction rotateYCounterclockwise(Direction direction) {
        return switch (direction.ordinal()) {
            case 2 -> Direction.WEST;
            case 5 -> Direction.NORTH;
            case 3 -> Direction.EAST;
            case 4 -> Direction.SOUTH;
            default -> throw new IllegalStateException("Unable to rotate " + direction);
        };
    }

    public static Axis getAxis(Direction direction) {
        return switch (direction) {
            case DOWN, UP -> Axis.Y;
            case NORTH, SOUTH -> Axis.Z;
            case WEST, EAST -> Axis.X;
        };
    }

    public enum Axis {
        X,
        Y,
        Z
    }
}
