package melonslise.locks.common.network.toclient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateLockablePacket(int id ,boolean locked) implements CustomPacketPayload {
    public static final Type<UpdateLockablePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "update_lockable"));


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateLockablePacket pkt, LocalPlayer localPlayer) {
        LocksComponents.LOCKABLE_HANDLER.get(localPlayer.level()).getLoaded().get(pkt.id).lock.setLocked(pkt.locked);
    }

    public UpdateLockablePacket(Lockable lkb) {
        this(lkb.id, lkb.lock.isLocked());
    }

    public static final Codec<UpdateLockablePacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("id").forGetter(UpdateLockablePacket::id),
                    Codec.BOOL.fieldOf("locked").forGetter(UpdateLockablePacket::locked)
            ).apply(instance, UpdateLockablePacket::new)
    );

    public static final StreamCodec<ByteBuf,UpdateLockablePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,UpdateLockablePacket::id,
            ByteBufCodecs.BOOL,UpdateLockablePacket::locked,
            UpdateLockablePacket::new
    );


}