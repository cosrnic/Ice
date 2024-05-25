package dev.cosrnic.ice.rules;

import dev.cosrnic.ice.rules.utils.GenericBlockPlacementRule;
import dev.cosrnic.ice.rules.utils.States;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TrapdoorRule extends GenericBlockPlacementRule {
    public TrapdoorRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        BlockFace placementFace = placementState.blockFace();
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = BlockFace.fromYaw(playerPos.yaw()).toDirection().opposite();
        Point cursorPos = Objects.requireNonNullElse(placementState.cursorPosition(), Vec.ZERO);
        BlockFace facing = BlockFace.fromDirection(direction);

        BlockFace half = placementFace == BlockFace.BOTTOM
                ||
                placementFace != BlockFace.TOP
                        &&
                        cursorPos.y() > 0.5
                ? BlockFace.TOP
                : BlockFace.BOTTOM;

        Block block = placementState.block().withProperties(Map.of(
                States.HALF, half.name().toLowerCase(),
                States.FACING, facing.name().toLowerCase()
        ));

        return canPlaceAt(placementState.instance(), placementState.placePosition(), placementState.blockFace().toDirection().opposite(), placementState.isPlayerShifting()) ? block : null;
    }
}
