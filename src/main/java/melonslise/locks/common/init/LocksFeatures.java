package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.worldgen.feature.LocksFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class LocksFeatures {

    public static final LocksFeature BOULDER_TRAP = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Locks.ID, "locks"), new LocksFeature(NoneFeatureConfiguration.CODEC));

    public static void register()
    {

    }
}
