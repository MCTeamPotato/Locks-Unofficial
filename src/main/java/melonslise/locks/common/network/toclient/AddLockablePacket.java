package melonslise.locks.common.network.toclient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AddLockablePacket(Lockable.LockableRecord lockable) implements CustomPacketPayload {
//    public static final PacketType<AddLockablePacket> TYPE = PacketType.create(ID, AddLockablePacket::new);

    public static final Type<AddLockablePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "add_lockable"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AddLockablePacket pkt, LocalPlayer localPlayer) {
        LocksComponents.LOCKABLE_HANDLER.get(localPlayer.level()).add(new Lockable(pkt.lockable), localPlayer.level());
    }


//    public AddLockablePacket(FriendlyByteBuf buf) {
//        this(Lockable.fromBuf(buf));
//    }

    public static final Codec<AddLockablePacket> CODEC = RecordCodecBuilder.create(objectInstance ->
        objectInstance.group(
                Lockable.CODEC.fieldOf("lockable").forGetter(AddLockablePacket::lockable)
        ).apply(objectInstance, AddLockablePacket::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,AddLockablePacket> STREAM_CODEC = StreamCodec.composite(
            Lockable.STREAM_CODEC,AddLockablePacket::lockable,
            AddLockablePacket::new
    );

}