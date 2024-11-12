package melonslise.locks.common.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class LockableInfo
{
	public final Cuboid6i bb;
	public final Lock lock;
	public final Transform tr;
	public final ItemStack stack;
	public final int id;

	public LockableInfo(Cuboid6i bb, Lock lock, Transform tr, ItemStack stack, int id)
	{
		this.bb = bb;
		this.lock = lock;
		this.tr = tr;
		this.stack = stack;
		this.id = id;
	}

	public static LockableInfo fromNbt(HolderLookup.Provider provider, CompoundTag nbt)
	{
		return new LockableInfo(Cuboid6i.fromNbt(nbt.getCompound(Lockable.KEY_BB)), Lock.fromNbt(nbt.getCompound(Lockable.KEY_LOCK)), Transform.values()[(int) nbt.getByte(Lockable.KEY_TRANSFORM)], ItemStack.parseOptional(provider,nbt.getCompound(Lockable.KEY_STACK)), nbt.getInt(Lockable.KEY_ID));
	}

	public static CompoundTag toNbt(HolderLookup.Provider provider,LockableInfo lkb)
	{
		CompoundTag nbt = new CompoundTag();
		nbt.put(Lockable.KEY_BB, Cuboid6i.toNbt(lkb.bb));
		nbt.put(Lockable.KEY_LOCK, Lock.toNbt(lkb.lock));
		nbt.putByte(Lockable.KEY_TRANSFORM, (byte) lkb.tr.ordinal());
		nbt.put(Lockable.KEY_STACK, lkb.stack.save(provider));
		nbt.putInt(Lockable.KEY_ID, lkb.id);
		return nbt;
	}
}