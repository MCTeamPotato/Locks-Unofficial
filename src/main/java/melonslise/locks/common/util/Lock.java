package melonslise.locks.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonslise.locks.common.item.LockItem;
import melonslise.locks.common.item.LockingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Lock extends Observable
{
	public static final Codec<LockRecord> CODEC = RecordCodecBuilder.create(lockInstance ->
			lockInstance.group(
					Codec.INT.fieldOf("id").forGetter(LockRecord::id),
					Codec.list(Codec.BYTE).fieldOf("combo").forGetter(LockRecord::combo),
					Codec.BOOL.fieldOf("locked").forGetter(LockRecord::locked)
			).apply(lockInstance, LockRecord::new)
	);

	public static final StreamCodec<ByteBuf,LockRecord> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,LockRecord::id,
			ByteBufCodecs.fromCodec(Codec.list(Codec.BYTE)),LockRecord::combo,
			ByteBufCodecs.BOOL,LockRecord::locked,
			LockRecord::new
	);

	public record LockRecord(int id,List<Byte> combo,boolean locked){
	}


	public LockRecord lockRecord;
//	public final int id;
//	// index is the order, value is the pin number
//	protected final byte[] combo;
//	protected boolean locked;

	//  TODO if lock is reshuffled any time other than during creation, then next time it is loaded it will have the initial combination and not the newly reshuffled one. Thankfully reshuffling like that does happen, but this should be changed if it does happen
	public final Random rng;

	public Lock(LockRecord lock)
	{
		this.lockRecord = lock;
		this.rng = new Random(lock.id);
	}
	public Lock(int id, int length, boolean locked)
	{
		this.rng = new Random(id);
		this.lockRecord = new LockRecord(id, this.shuffle(length), locked);
	}

	public static Lock gen(ItemStack stack)
	{
		return new Lock(LockingItem.getOrSetId(stack), LockItem.getOrSetLength(stack), true);
	}
	public static Lock from(ItemStack stack)
	{
		return new Lock(LockingItem.getOrSetId(stack), LockItem.getOrSetLength(stack), !LockItem.isOpen(stack));
	}

	public static final String KEY_ID = "Id", KEY_LENGTH = "Length", KEY_LOCKED = "Locked";

	public static Lock fromNbt(CompoundTag nbt)
	{
		return new Lock(nbt.getInt(KEY_ID), nbt.getByte(KEY_LENGTH), nbt.getBoolean(KEY_LOCKED));
	}

	public static CompoundTag toNbt(Lock lock)
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt(KEY_ID, lock.lockRecord.id);
		nbt.putByte(KEY_LENGTH, (byte) lock.lockRecord.combo.size());
		nbt.putBoolean(KEY_LOCKED, lock.lockRecord.locked);
		return nbt;
	}

	public List<Byte> shuffle(int length)
	{
		List<Byte> combo = new ArrayList<>(length);
		for(byte a = 0; a < length; ++a)
			combo.add(a);
		LocksUtil.shuffle(combo, this.rng);
		return combo;
	}

	/*
	public byte[] inverse(byte[] combination)
	{
		byte[] lookup = new byte[combination.length];
		for(byte a = 0; a < combination.length; ++a)
			lookup[combination[a]] = a;
		return lookup;
	}
	*/

	public int getLength()
	{
		return this.lockRecord.combo.size();
	}

	public boolean isLocked()
	{
		return this.lockRecord.locked;
	}

	public void setLocked(boolean locked)
	{
		if(this.lockRecord.locked == locked)
			return;
		this.lockRecord = new LockRecord(this.lockRecord.id, this.lockRecord.combo, locked);
		this.setChanged();
		this.notifyObservers();
	}

	public int getPin(int index)
	{
		return this.lockRecord.combo.get(index);
	}

	public boolean checkPin(int index, int pin)
	{
		return this.getPin(index) == pin;
	}
}