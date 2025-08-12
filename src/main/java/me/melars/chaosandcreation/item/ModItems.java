package me.melars.chaosandcreation.item;

import me.melars.chaosandcreation.ChaosandCreation;
import me.melars.chaosandcreation.item.custom.LegoIgniterItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ChaosandCreation.MODID);

    public static final DeferredItem<Item> LEGO_IGNITER = ITEMS.register("lego_igniter",
            () -> new LegoIgniterItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
