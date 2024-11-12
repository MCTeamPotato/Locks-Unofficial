package melonslise.locks.client.gui.sprite.action;

import melonslise.locks.client.gui.sprite.Sprite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//暂停动作
@Environment(EnvType.CLIENT)
public class WaitAction<S extends Sprite> extends TimedAction<S>
{
	public static <Z extends Sprite> WaitAction<Z> ticks(int ticks)
	{
		return (WaitAction<Z>) new WaitAction().time(ticks);
	}
}