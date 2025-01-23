package melonslise.locks.common.capability;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksCapabilities;
import melonslise.locks.common.util.Lockable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

/*
 * Internal storage for lockables with almost no handling logic
 * Also stores lockables which are shared by multiple chunks. Duplicate shared lockables are handled by checking if they have already been loaded before
 */
@AutoRegisterCapability
public class LockableStorage implements ILockableStorage {
    public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "lockable_storage");

    public final LevelChunk chunk;

    public Int2ObjectMap<Lockable> lockables = new Int2ObjectLinkedOpenHashMap<>();

    public LockableStorage(LevelChunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public Int2ObjectMap<Lockable> get() {
        return this.lockables;
    }

    @Override
    public void add(Lockable lkb) {
        this.lockables.put(lkb.id, lkb);
        this.chunk.setUnsaved(true);
    }

    @Override
    public void remove(int id) {
        this.lockables.remove(id);
        this.chunk.setUnsaved(true);
    }

    @Override
    public ListTag serializeNBT() {
        ListTag list = new ListTag();
        for (Lockable lkb : this.lockables.values())
            list.add(Lockable.toNbt(lkb));
        return list;
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        ILockableHandler handler = this.chunk.getLevel().getCapability(LocksCapabilities.LOCKABLE_HANDLER).orElse(null);
        Int2ObjectMap<Lockable> lkbs = handler.getLoaded();
        for (int a = 0; a < nbt.size(); ++a) {
            CompoundTag nbt1 = nbt.getCompound(a);
            Lockable lkb = lkbs.get(Lockable.idFromNbt(nbt1));
            if (lkb == lkbs.defaultReturnValue()) {
				//if (lkb==null) return;
                lkb = Lockable.fromNbt(nbt1);
                lkb.addObserver(handler);
                lkbs.put(lkb.id, lkb);
            }
            this.lockables.put(lkb.id, lkb);
        }
    }
}
