package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.enchantment.ComplexityEnchantment;
import melonslise.locks.common.enchantment.ShockingEnchantment;
import melonslise.locks.common.enchantment.SturdyEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class LocksEnchantments
{

	public static final Enchantment
		SHOCKING = add("shocking", new ShockingEnchantment()),
		STURDY = add("sturdy", new SturdyEnchantment()),
		COMPLEXITY = add("complexity", new ComplexityEnchantment());

	private LocksEnchantments() {}

	public static void register()
	{
	}

	public static Enchantment add(String name, Enchantment ench)
	{
		return Registry.register(BuiltInRegistries.ENCHANTMENT,new ResourceLocation(Locks.ID,name),ench);
	}
}