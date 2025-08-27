package me.melars.chaosandcreation.util;

import me.melars.chaosandcreation.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public final class LegoPortalShape {
    private LegoPortalShape() {}

    public static final int INNER_W = 2;
    public static final int INNER_H = 3;

    private static Direction horiz(Direction.Axis axis) {
        return (axis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;
    }

    private static boolean isFrame(LevelReader level, BlockPos pos) {
        //return level.getBlockState(pos).is(ModBlocks.LEGO_BLOCK.get());
        return level.getBlockState(pos).is(ModBlocks.LEGO_FRAME_BLOCK.get());
    }

    private static boolean isPortal(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.LEGO_PORTAL.get());
    }

    // legos inside not allowed >:[
    private static boolean isReplaceable(LevelReader level, BlockPos pos) {
        BlockState st = level.getBlockState(pos);
        if (st.is(ModBlocks.LEGO_BLOCK.get()) || st.is(ModBlocks.LEGO_FRAME_BLOCK.get()))
            return false; // never treat frame as interior
        return st.canBeReplaced();
    }

    // make sure entire interior is clear/replacable
    private static boolean interiorClear(LevelReader level, BlockPos bl, Direction.Axis axis) {
        Direction H = horiz(axis);
        for (int y = 0; y < INNER_H; y++) {
            for (int w = 0; w < INNER_W; w++) {
                if (!isReplaceable(level, bl.relative(H, w).above(y))) return false;
            }
        }
        return true;
    }

    private static boolean frameRingIntact(LevelReader level, BlockPos bl, Direction.Axis axis) {
        Direction H = horiz(axis);
        // bottom/top rows
        for (int w = 0; w < INNER_W; w++) {
            if (!isFrame(level, bl.relative(H, w).below()))            return false; // bottom
            if (!isFrame(level, bl.relative(H, w).above(INNER_H)))     return false; // top
        }
        // left/right columns
        for (int y = 0; y < INNER_H; y++) {
            if (!isFrame(level, bl.relative(H, -1).above(y)))          return false; // left
            if (!isFrame(level, bl.relative(H,  INNER_W).above(y)))    return false; // right
        }
        return true;
    }

    // slide left & down through interior to locate BL
    private static BlockPos slideToBL(LevelReader level, BlockPos anyInterior, Direction.Axis axis) {
        Direction H = horiz(axis);
        BlockPos p = anyInterior;
        while (isReplaceable(level, p.relative(H.getOpposite()))) p = p.relative(H.getOpposite());
        while (isReplaceable(level, p.below()))                  p = p.below();
        return p;
    }

    // if correct size opening exists for axis around anyInterior, return its BL; else null
    private static BlockPos findBLForAxis(LevelReader level, BlockPos anyInterior, Direction.Axis axis) {
        if (!isReplaceable(level, anyInterior)) return null;
        BlockPos bl = slideToBL(level, anyInterior, axis);
        if (!interiorClear(level, bl, axis))   return null;
        if (!frameRingIntact(level, bl, axis)) return null;
        return bl;
    }

    // try both axes, return (axis, BL) for the first valid opening or null
    public static Result findOpening(LevelReader level, BlockPos anyInterior) {
        BlockPos blX = findBLForAxis(level, anyInterior, Direction.Axis.X);
        if (blX != null) return new Result(Direction.Axis.X, blX);
        BlockPos blZ = findBLForAxis(level, anyInterior, Direction.Axis.Z);
        if (blZ != null) return new Result(Direction.Axis.Z, blZ);
        return null;
    }

    // for existing portal cell, rederive BL of portal and make sure frame still exists
    public static boolean portalStillValid(LevelReader level, BlockPos anyPortalCell, Direction.Axis axis) {
        if (!isPortal(level, anyPortalCell)) return false;
        Direction H = horiz(axis);

        // slide through portal blocks to BL of portal
        BlockPos p = anyPortalCell;
        while (isPortal(level, p.relative(H.getOpposite()))) p = p.relative(H.getOpposite());
        while (isPortal(level, p.below()))                  p = p.below();

        // verify full portal exists (may not need but wtv)
        for (int y = 0; y < INNER_H; y++) {
            for (int w = 0; w < INNER_W; w++) {
                if (!isPortal(level, p.relative(horiz(axis), w).above(y))) return false;
            }
        }
        // and the ring is intact
        return frameRingIntact(level, p, axis);
    }

    // fill interior with portal state
    public static void fillInterior(LevelAccessor level, BlockPos bl, Direction.Axis axis, BlockState portalState) {
        Direction H = horiz(axis);
        for (int y = 0; y < INNER_H; y++) {
            for (int w = 0; w < INNER_W; w++) {
                level.setBlock(bl.relative(H, w).above(y), portalState, Block.UPDATE_ALL);
            }
        }
    }

    // prob could be record but idc
    public static final class Result {
        public final Direction.Axis axis;
        public final BlockPos bl;
        public Result(Direction.Axis axis, BlockPos bl) {
            this.axis = Objects.requireNonNull(axis);
            this.bl   = Objects.requireNonNull(bl);
        }
    }
}
