package melonslise.locks.common.network.toclient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksComponents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RemoveLockablePacket(int id) implements CustomPacketPayload {


    public static final Type<RemoveLockablePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "remove_lockable"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RemoveLockablePacket pkt, LocalPlayer localPlayer) {
        LocksComponents.LOCKABLE_HANDLER.get(localPlayer.level()).remove(pkt.id);
    }

    public static final Codec<RemoveLockablePacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("id").forGetter(RemoveLockablePacket::id)
            ).apply(instance, RemoveLockablePacket::new)
    );

    public static final StreamCodec<ByteBuf,RemoveLockablePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,RemoveLockablePacket::id,
            RemoveLockablePacket::new
    );
}