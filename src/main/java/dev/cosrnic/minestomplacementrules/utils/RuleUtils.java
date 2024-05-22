package dev.cosrnic.minestomplacementrules.utils;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;

import java.util.List;

public class RuleUtils {

    public static final String HALF = "half";
    public static final String FACING = "facing";
    public static final String FACE = "face";
    public static final String SHAPE = "shape";
    public static final String WATERLOGGED = "waterlogged";
    public static final String NORTH = "north";
    public static final String EAST = "east";
    public static final String SOUTH = "south";
    public static final String WEST = "west";

    private static final List<String> INTERACTION_BLOCK_NAMES = List.of(
            "_button", // does a check for endsWith or if this list contains the block name
            "minecraft:anvil",
            "_anvil"

    );

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

    public static boolean canPlaceAt(Block.Getter instance, Point blockPos, Direction direction, boolean shifting) {
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

    public static Direction getDirection(Block block) {
        if (block.getProperty(RuleUtils.FACE) == null) return Direction.NORTH;
        return switch (block.getProperty(RuleUtils.FACE)) {
            case "ceiling" -> Direction.DOWN;
            case "floor" -> Direction.UP;
            default -> RuleUtils.getFacing(block).toDirection();
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
