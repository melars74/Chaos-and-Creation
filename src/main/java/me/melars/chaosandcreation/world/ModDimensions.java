package me.melars.chaosandcreation.world;

import me.melars.chaosandcreation.ChaosandCreation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

public final class ModDimensions {
    public static final ResourceKey<Level> LEGO_LEVEL = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(ChaosandCreation.MODID, "lego"));

    public static final ResourceKey<LevelStem> LEGO_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(ChaosandCreation.MODID, "lego"));
}
