package me.melars.chaosandcreation.world;

import me.melars.chaosandcreation.ChaosandCreation;
import me.melars.chaosandcreation.block.ModBlocks;
import me.melars.chaosandcreation.block.custom.LegoBlock;
import me.melars.chaosandcreation.world.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;

//@EventBusSubscriber(modid = ChaosandCreation.MODID)
public class LegoSurfaceFixer {

//    @SubscribeEvent
//    public static void onChunkLoad(ChunkEvent.Load event) {
//        if (!(event.getLevel() instanceof Level level)) return;
//        if (level.isClientSide) return;
//        if (!level.dimension().equals(ModDimensions.LEGO_LEVEL)) return;
//        if (!(event.getChunk() instanceof LevelChunk chunk)) return;
//
//        final int x0 = chunk.getPos().getMinBlockX();
//        final int z0 = chunk.getPos().getMinBlockZ();
//
//        // Iterate columns once; flip ON_TOP only on the visible LEGO block
//        for (int dx = 0; dx < 16; dx++) {
//            for (int dz = 0; dz < 16; dz++) {
//                int x = x0 + dx;
//                int z = z0 + dz;
//
//                // WORLD_SURFACE returns the first non-air at that column (top solid)
//                BlockPos top = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z)).below();
//                BlockState st = level.getBlockState(top);
//
//                if (st.is(ModBlocks.LEGO_BLOCK.get())) {
//                    BlockState above = level.getBlockState(top.above());
//                    boolean blockAbove = !(above.isAir() || above.is(ModBlocks.LEGO_PORTAL.get()));
//                    boolean newOnTop = !blockAbove;
//
//                    // Only write if different; client update only (no neighbor cascade)
//                    if (st.getValue(LegoBlock.ON_TOP) != newOnTop) {
//                        level.setBlock(top, st.setValue(LegoBlock.ON_TOP, newOnTop), Block.UPDATE_CLIENTS);
//                    }
//                }
//            }
//        }
//    }
}
