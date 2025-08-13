package me.melars.chaosandcreation.block.custom;

import me.melars.chaosandcreation.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

// TODO: may want to make a tag for this block, look into it

public class LegoBlock extends Block {
    public static final BooleanProperty ON_TOP = BooleanProperty.create("on_top");

    public LegoBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ON_TOP, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ON_TOP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState above = level.getBlockState(pos.above());

        boolean blockAbove = !(above.isAir() || above.is(ModBlocks.LEGO_PORTAL.get()));
        return this.defaultBlockState().setValue(ON_TOP, !blockAbove);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (dir == Direction.UP) {
            boolean blockAbove = !(neighborState.isAir() || neighborState.is(ModBlocks.LEGO_PORTAL.get()));
            return state.setValue(ON_TOP, !blockAbove);
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }
}
