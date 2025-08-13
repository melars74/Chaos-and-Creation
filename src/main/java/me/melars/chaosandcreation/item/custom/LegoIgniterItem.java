package me.melars.chaosandcreation.item.custom;

import me.melars.chaosandcreation.block.ModBlocks;
import me.melars.chaosandcreation.util.LegoPortalShape;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LegoIgniterItem extends Item{

    public LegoIgniterItem(Properties properties){
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        ServerLevel server = (ServerLevel) level;

        BlockPos click = ctx.getClickedPos().relative(ctx.getClickedFace());

        LegoPortalShape.Result portal = LegoPortalShape.findOpening(server, click);
        if (portal == null) return InteractionResult.FAIL;

        // good portal
        BlockState portalState = ModBlocks.LEGO_PORTAL.get()
                .defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_AXIS, portal.axis);

        LegoPortalShape.fillInterior(server, portal.bl, portal.axis, portalState);

        if (ctx.getPlayer() != null) {
            ctx.getItemInHand().hurtAndBreak(1, ctx.getPlayer(), LivingEntity.getSlotForHand(ctx.getHand()));
        }
        return InteractionResult.CONSUME; // in case more behavior added to this
    }

}


