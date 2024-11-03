package melonslise.locks.client.init;

import melonslise.locks.client.gui.KeyRingScreen;
import melonslise.locks.client.gui.LockPickingScreen;
import melonslise.locks.common.init.LocksContainerTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public final class LocksScreens
{
	private LocksScreens() {}

	public static void register()
	{
		MenuScreens.register(LocksContainerTypes.LOCK_PICKING, LockPickingScreen::new);
		MenuScreens.register(LocksContainerTypes.KEY_RING, KeyRingScreen::new);
	}
}