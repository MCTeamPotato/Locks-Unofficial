package melonslise.locks.client.gui.sprite.action;

import melonslise.locks.client.gui.sprite.Sprite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public abstract class SingleCallbackAction<S extends Sprite> implements IAction<S>
{
	public BiConsumer<IAction<S>, S> cb;

	@Override
	public void finish(S sprite)
	{
		if(this.cb != null)
			this.cb.accept(this, sprite);
	}

	@Override
	public IAction<S> then(BiConsumer<IAction<S>, S> cb)
	{
		this.cb = cb;
		return this;
	}
}