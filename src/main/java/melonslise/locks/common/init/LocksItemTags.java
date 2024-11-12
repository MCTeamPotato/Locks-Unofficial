package melonslise.locks.common.init;

import melonslise.locks.Locks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class LocksItemTags
{
	private LocksItemTags() {}

	public static final TagKey<Item>
		KEYS = bind("keys"),
		LOCKS = bind("locks"),
		LOCK_PICKS = bind("lock_picks");

	public static TagKey<Item> bind(String name)
	{
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Locks.ID,name));
	}
}