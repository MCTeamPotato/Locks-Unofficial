package melonslise.locks.client.init;

import melonslise.locks.Locks;
import melonslise.locks.common.components.ItemHandler;
import melonslise.locks.common.components.interfaces.IItemHandler;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.init.LocksItems;
import melonslise.locks.common.item.LockItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class LocksItemModelsProperties
{
	private LocksItemModelsProperties() {}

	public static void register()
	{
		ItemProperties.register(LocksItems.KEY_RING, ResourceLocation.fromNamespaceAndPath(Locks.ID, "keys"), (stack, world, entity, speed) ->
		{
			ItemHandler inv = stack.get(LocksComponents.ITEM_HANDLER);
				if(inv!=null){
					int keys = 0;
					for(int a = 0; a < inv.getSlots(); ++a)
						if(!inv.getStackInSlot(a).isEmpty())
							++keys;
					return (float) keys / inv.getSlots();
				}
				return 0f;
		});
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Locks.ID, "open");
		ClampedItemPropertyFunction getter = (stack, world, entity, speed) -> LockItem.isOpen(stack) ? 1f : 0f;
		ItemProperties.register(LocksItems.WOOD_LOCK, id, getter);
		ItemProperties.register(LocksItems.IRON_LOCK, id, getter);
		ItemProperties.register(LocksItems.STEEL_LOCK, id, getter);
		ItemProperties.register(LocksItems.GOLD_LOCK, id, getter);
		ItemProperties.register(LocksItems.DIAMOND_LOCK, id, getter);
	}
}