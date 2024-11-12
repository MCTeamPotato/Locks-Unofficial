package melonslise.locks.common.components;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import melonslise.locks.Locks;
import melonslise.locks.common.components.interfaces.ILockableHandler;
import melonslise.locks.common.components.interfaces.ILockableStorage;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Objects;

/*
 * Internal storage for lockables with almost no handling logic
 * Also stores lockables which are shared by multiple chunks. Duplicate shared lockables are handled by checking if they have already been loaded before
 */
public class LockableStorage implements ILockableStorage
{
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Locks.ID, "lockable_storage");

	public final ChunkAccess chunk;

	public Int2ObjectMap<Lockable> lockables = new Int2ObjectLinkedOpenHashMap<Lockable>();

	public LockableStorage(ChunkAccess chunk)
	{
		this.chunk = chunk;
	}

	@Override
	public Int2ObjectMap<Lockable> get()
	{
		return this.lockables;
	}

	@Override
	public void add(Lockable lkb)
	{
		this.lockables.put(lkb.id, lkb);
		this.chunk.setUnsaved(true);
	}

	@Override
	public void remove(int id)
	{
		this.lockables.remove(id);
		this.chunk.setUnsaved(true);
	}

	@Override
	public void readFromNbt(CompoundTag nbt, HolderLookup.Provider provider) {
		int size = nbt.getInt("LockablesSize");
		ListTag lockables = nbt.getList("Lockables",size);
		ILockableHandler handler;
		if(this.chunk instanceof LevelChunk levelChunk){
			handler =LocksComponents.LOCKABLE_HANDLER.get(levelChunk.getLevel());
		}else {
			handler =LocksComponents.LOCKABLE_HANDLER.get(Minecraft.getInstance().level);
		}
		Int2ObjectMap<Lockable> lkbs = handler.getLoaded();
		for(int a = 0; a < lockables.size(); ++a)
		{
			CompoundTag nbt1 = lockables.getCompound(a);
			Lockable lkb = lkbs.get(Lockable.idFromNbt(nbt1));
			if(lkb == lkbs.defaultReturnValue())
			{
				if (lkb==null) return;
				lkb = Lockable.fromNbt(provider,nbt1);
				lkb.addObserver(handler);
				lkbs.put(lkb.id, lkb);
			}
			this.lockables.put(lkb.id, lkb);
		}

	}

	@Override
	public void writeToNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
		ListTag list = new ListTag();
		for(Lockable lkb : this.lockables.values())
			list.add(Lockable.toNbt(provider,lkb));
		compoundTag.put("Lockables", list);
		compoundTag.putInt("LockablesSize", this.lockables.size());
	}
}
