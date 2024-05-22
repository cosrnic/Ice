package dev.cosrnic.minestomplacementrules.rules;

import dev.cosrnic.minestomplacementrules.utils.RuleUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static dev.cosrnic.minestomplacementrules.utils.RuleUtils.*;

public class AnvilRule extends BlockPlacementRule {

    public AnvilRule(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull BlockPlacementRule.PlacementState placementState) {
        Pos playerPos = Objects.requireNonNullElse(placementState.playerPosition(), Pos.ZERO);
        Direction direction = rotateYCounterclockwise(BlockFace.fromYaw(playerPos.yaw()).toDirection());
        BlockFace facing = BlockFace.fromDirection(direction);

        Block block = placementState.block().withProperty(RuleUtils.FACING, facing.name().toLowerCase());

        return canPlaceAt(placementState.instance(), placementState.placePosition(), placementState.blockFace().toDirection().opposite(), placementState.isPlayerShifting()) ? block : null;
    }
}
