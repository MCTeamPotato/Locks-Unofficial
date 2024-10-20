package melonslise.locks.mixin;

import melonslise.locks.common.util.ILockableProvider;
import melonslise.locks.common.util.Lockable;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChunkPos.class)
public class ChunkPrimerMixin implements ILockableProvider
{
	private final List<Lockable> lockableList = new ArrayList<>();

	@Override
	public List<Lockable> getLockables()
	{
		return this.lockableList;
	}
}