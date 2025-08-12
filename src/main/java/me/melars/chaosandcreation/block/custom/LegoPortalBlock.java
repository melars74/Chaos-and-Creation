package me.melars.chaosandcreation.block.custom;

import me.melars.chaosandcreation.world.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LegoPortalBlock extends Block {

    private static final VoxelShape X_SHAPE = Block.box(0, 0, 7, 16, 16, 9);
    private static final VoxelShape Z_SHAPE = Block.box(7, 0, 0, 9, 16, 16);

    public LegoPortalBlock(Properties properties) {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0f)
                .lightLevel(state -> 11)
                .pushReaction(PushReaction.BLOCK));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_AXIS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                               CollisionContext ctx) {
        return state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X
                ? X_SHAPE : Z_SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide() || !(entity instanceof ServerPlayer player)) return;

        MinecraftServer server = level.getServer();

        boolean inLego = player.level().dimension().equals(ModDimensions.LEGO_LEVEL);

        ServerLevel target = server.getLevel(inLego ? Level.OVERWORLD : ModDimensions.LEGO_LEVEL);
        if (target == null)  return;

        Vec3 dest = player.adjustSpawnLocation(target, target.getSharedSpawnPos()).getBottomCenter();

        DimensionTransition.PostDimensionTransition post =
                DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET);

        player.changeDimension(new DimensionTransition(
                target,
                dest,
                Vec3.ZERO,
                player.getYRot(),
                player.getXRot(),
                post
        ));
    }

}
