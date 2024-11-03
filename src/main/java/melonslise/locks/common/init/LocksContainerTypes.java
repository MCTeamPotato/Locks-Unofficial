package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.container.KeyRingContainer;
import melonslise.locks.common.container.LockPickingContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class LocksContainerTypes
{


	public static final ExtendedScreenHandlerType<LockPickingContainer>
			LOCK_PICKING = new ExtendedScreenHandlerType<>(LockPickingContainer.FACTORY);

	public static final ExtendedScreenHandlerType<KeyRingContainer>
			KEY_RING = new ExtendedScreenHandlerType<>(KeyRingContainer.FACTORY);

	public static final MenuType<LockPickingContainer>
			LOCK_PICKING_TYPE = add("lock_picking", LOCK_PICKING);

	public static final MenuType<KeyRingContainer>
			KEY_RING_TYPE= add("key_ring", KEY_RING);

	private LocksContainerTypes() {}

	public static void register()
	{
	}

	public static <T extends AbstractContainerMenu> MenuType<T> add(String name, MenuType<T> type)
	{
		return Registry.register(BuiltInRegistries.MENU,new ResourceLocation(Locks.ID,name),type);
	}
}