package me.melars.chaosandcreation.item.custom;

import me.melars.chaosandcreation.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

public class LegoIgniterItem extends Item{

    public LegoIgniterItem(Properties properties){
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        ServerLevel server = (ServerLevel) level;
        BlockPos start = ctx.getClickedPos().relative(ctx.getClickedFace());

        // try both axes for 2x3 opening
        for(Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
            List<BlockPos> interior = tryFindInner2x3(server, start, axis);
            if (!interior.isEmpty()) {
                BlockState portal = ModBlocks.LEGO_PORTAL.get().defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
                for (BlockPos p : interior)
                    server.setBlock(p, portal, Block.UPDATE_ALL_IMMEDIATE);
                //damage igniter
                if (ctx.getPlayer() != null) {
                    ctx.getItemInHand().hurtAndBreak(1, ctx.getPlayer(), LivingEntity.getSlotForHand(ctx.getHand()));
                }
                return InteractionResult.CONSUME;
            }
        }
         return InteractionResult.FAIL;
    }

    // validator for 2x3 LEGO_BLOCK frame with air/replacable interior
    private List<BlockPos> tryFindInner2x3(ServerLevel level, BlockPos any, Direction.Axis axis) {
        // find the lowest possible “bottom-left interior” near the clicked spot:
        // scan a small cube around the click and test rectangles.
        int range = 2; // small search radius around click
        for (int dy = -2; dy <= 2; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos candidate = any.offset(dx, dy, dz);
                    if (isValidInner2x3(level, candidate, axis)) {
                        return collectInterior2x3(candidate, axis);
                    }
                }
            }
        }
        return List.of();
    }

    private boolean isValidInner2x3(ServerLevel level, BlockPos innerBL, Direction.Axis axis) {
        // inner size: width=2, height=3
        // frame ring must be lego_block
        // decide horizontal step based on axis
        Direction H1 = (axis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;
        Direction UP = Direction.UP;

        // Check interior 2x3 is clear
        for (int y = 0; y < 3; y++) for (int w = 0; w < 2; w++) {
            BlockPos p = innerBL.relative(H1, w).relative(UP, y);
            if (!level.getBlockState(p).canBeReplaced()) return false;
        }

        // frame surrounding the 2x3 opening => 4x5 outer
        // bottom/top rows 4 long
        for (int w = -1; w <= 2; w++) {
            if (!isLego(level, innerBL.relative(H1, w).relative(UP, -1))) return false;
            if (!isLego(level, innerBL.relative(H1, w).relative(UP, 3)))  return false;
        }
        // left/right columns 5 tall
        for (int y = -1; y <= 3; y++) {
            if (!isLego(level, innerBL.relative(H1, -1).relative(UP, y))) return false;
            if (!isLego(level, innerBL.relative(H1,  2).relative(UP, y))) return false;
        }
        return true;
    }

    private boolean isLego(ServerLevel level, BlockPos p) {
        return level.getBlockState(p).is(ModBlocks.LEGO_BLOCK.get());
    }

    private List<BlockPos> collectInterior2x3(BlockPos innerBL, Direction.Axis axis) {
        Direction H1 = (axis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;
        Direction UP = Direction.UP;
        List<BlockPos> out = new ArrayList<>();
        for (int y = 0; y < 3; y++) for (int w = 0; w < 2; w++) {
            out.add(innerBL.relative(H1, w).relative(UP, y));
        }
        return out;
    }
}


