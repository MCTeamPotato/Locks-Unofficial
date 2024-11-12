package melonslise.locks.common.network.toclient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import melonslise.locks.Locks;
import melonslise.locks.common.components.interfaces.ILockableHandler;
import melonslise.locks.common.components.interfaces.ILockableStorage;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;

public record AddLockableToChunkPacket(Lockable.LockableRecord lockable,int x,int z) implements CustomPacketPayload {
    


    public static final Type<AddLockableToChunkPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "add_lockable2chunk"));

    public static void handle(AddLockableToChunkPacket pkt, LocalPlayer localPlayer) {
        Level level = localPlayer.level();
        if (level.getChunk(pkt.x, pkt.z) instanceof EmptyLevelChunk) return;
        ILockableStorage st = LocksComponents.LOCKABLE_STORAGE.get(level.getChunk(pkt.x, pkt.z));
        ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(level);
        Int2ObjectMap<Lockable> lkbs = handler.getLoaded();
        Lockable lkb = lkbs.get(pkt.lockable.id());
        if (lkb == lkbs.defaultReturnValue()) {
            lkb = new Lockable(pkt.lockable);
            lkb.addObserver(handler);
            lkbs.put(lkb.id, lkb);
        }
        st.add(lkb);
    }


    public static final Codec<AddLockableToChunkPacket> CODEC = RecordCodecBuilder.create(objectInstance ->
            objectInstance.group(
                    Lockable.CODEC.fieldOf("lockable").forGetter(AddLockableToChunkPacket::lockable),
                    Codec.INT.fieldOf("x").forGetter(AddLockableToChunkPacket::x),
                    Codec.INT.fieldOf("z").forGetter(AddLockableToChunkPacket::z)
            ).apply(objectInstance, AddLockableToChunkPacket::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,AddLockableToChunkPacket> STREAM_CODEC = StreamCodec.composite(
            Lockable.STREAM_CODEC,AddLockableToChunkPacket::lockable,
            ByteBufCodecs.INT,AddLockableToChunkPacket::x,
            ByteBufCodecs.INT,AddLockableToChunkPacket::z,
            AddLockableToChunkPacket::new
    );

    public AddLockableToChunkPacket(Lockable lkb, int x, int z) {
        this(lkb.toRecord(),x,z);
    }

    public AddLockableToChunkPacket(Lockable lkb, ChunkPos pos) {
        this(lkb, pos.x, pos.z);
    }

    public AddLockableToChunkPacket(Lockable lkb, LevelChunk ch) {
        this(lkb, ch.getPos());
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}