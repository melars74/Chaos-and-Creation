package me.melars.chaosandcreation.block;

import me.melars.chaosandcreation.ChaosandCreation;
import me.melars.chaosandcreation.block.custom.LegoPortalBlock;
import me.melars.chaosandcreation.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ChaosandCreation.MODID);

    //TODO: blockstate for stacking (just make anything with a block on top of it
    //      be a cube with no studs on top)
    public static final DeferredBlock<Block> LEGO_BLOCK = registerBlock("lego_block",
            () -> new Block(BlockBehaviour.Properties.of()));

    // not registering item intentionally for portal
    public static final DeferredBlock<Block> LEGO_PORTAL = BLOCKS.register("lego_portal",
            () -> new LegoPortalBlock(BlockBehaviour.Properties.of().noCollission().lightLevel(s -> 11)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> b = BLOCKS.register(name, block);
        registerBlockItem(name, b);
        return b;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
