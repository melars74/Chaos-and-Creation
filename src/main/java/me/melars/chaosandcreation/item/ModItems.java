package me.melars.chaosandcreation.item;

import me.melars.chaosandcreation.ChaosandCreation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ChaosandCreation.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
