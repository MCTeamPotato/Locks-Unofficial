package melonslise.locks.common.network.toclient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonslise.locks.Locks;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksContainerTypes;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record TryPinResultPacket(boolean correct,boolean reset) implements CustomPacketPayload {

	public static final Type<TryPinResultPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "try_pin_result"));


	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(TryPinResultPacket pkt, LocalPlayer localPlayer) {
		AbstractContainerMenu container = localPlayer.containerMenu;
		if (container.getType() == LocksContainerTypes.LOCK_PICKING)
			((LockPickingContainer) container).handlePin(pkt.correct, pkt.reset);
	}

	public static final Codec<TryPinResultPacket> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.BOOL.fieldOf("correct").forGetter(TryPinResultPacket::correct),
					Codec.BOOL.fieldOf("reset").forGetter(TryPinResultPacket::reset)
			).apply(instance, TryPinResultPacket::new)
	);

	public static final StreamCodec<ByteBuf,TryPinResultPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,TryPinResultPacket::correct,
			ByteBufCodecs.BOOL,TryPinResultPacket::reset,
			TryPinResultPacket::new
	);


}