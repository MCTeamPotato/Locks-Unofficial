package melonslise.locks.common.init;

import melonslise.locks.Locks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class LocksSoundEvents
{
	public static final SoundEvent
		KEY_RING = add("key_ring"),
		LOCK_CLOSE = add("lock.close"),
		LOCK_OPEN = add("lock.open"),
		LOCK_RATTLE = add("lock.rattle"),
		PIN_FAIL = add("pin.fail"),
		PIN_MATCH = add("pin.match"),
		SHOCK = add("shock");

	private LocksSoundEvents() {}

	public static void register()
	{
	}

	public static SoundEvent add(String name)
	{
	 	return Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(Locks.ID, name), SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Locks.ID, name)));
	}
}