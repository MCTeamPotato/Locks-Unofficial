package melonslise.locks.common.init;

import melonslise.locks.Locks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public final class LocksEnchantments
{


	public static Enchantment.EnchantmentDefinition
			SHOCKING_Definition,
			STURDY_Definition,
			COMPLEXITY_Definition;
  	static{
		SHOCKING_Definition = Enchantment.definition(HolderSet.direct(LocksItems.HOLDERS),1,5, new Enchantment.Cost(7,10),new Enchantment.Cost(22,10),1, EquipmentSlotGroup.ANY);
		STURDY_Definition = Enchantment.definition(HolderSet.direct(LocksItems.HOLDERS),1,3, new Enchantment.Cost(5,15),new Enchantment.Cost(50,0),1, EquipmentSlotGroup.ANY);
		COMPLEXITY_Definition = Enchantment.definition(HolderSet.direct(LocksItems.HOLDERS),1,3, new Enchantment.Cost(7,10),new Enchantment.Cost(22,10),1, EquipmentSlotGroup.ANY);
	}

	public static final Holder<Enchantment>
			SHOCKING = add("shocking", SHOCKING_Definition),
			STURDY = add("sturdy", STURDY_Definition),
			COMPLEXITY = add("complexity", COMPLEXITY_Definition);


	private LocksEnchantments() {}

	public static void register()
	{
	}

	public static Holder<Enchantment> add(String name, Enchantment.EnchantmentDefinition ench)
	{
		return Holder.direct(Enchantment.enchantment(ench).build(ResourceLocation.fromNamespaceAndPath(Locks.ID,name)));
	}
}