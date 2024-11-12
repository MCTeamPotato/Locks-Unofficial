package melonslise.locks.common.network.toserver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonslise.locks.Locks;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksContainerTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record TryPinPacket(byte pin) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<TryPinPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Locks.ID, "try_pin"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(TryPinPacket TryPinPacket, ServerPlayer serverPlayer) {
		AbstractContainerMenu container = serverPlayer.containerMenu;
		if (container.getType() == LocksContainerTypes.LOCK_PICKING)
			((LockPickingContainer) container).tryPin(TryPinPacket.pin);
	}

	public static final Codec<TryPinPacket> CODEC = RecordCodecBuilder.create(
			instance->instance.group(
					Codec.BYTE.fieldOf("pin").forGetter(TryPinPacket::pin)
			).apply(instance, TryPinPacket::new)
	);

	public static final StreamCodec<ByteBuf,TryPinPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BYTE,TryPinPacket::pin,
			TryPinPacket::new
	);

}