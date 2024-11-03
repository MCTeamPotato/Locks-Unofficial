package melonslise.locks.common.network.toserver;

import melonslise.locks.Locks;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksContainerTypes;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class TryPinPacket implements FabricPacket {
	public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "try_pin");
	public final byte pin;

	public static final PacketType<TryPinPacket> TYPE = PacketType.create(ID, TryPinPacket::new);

	public static class Handler implements ServerPlayNetworking.PlayPacketHandler<TryPinPacket>{
		@Override
		public void receive(TryPinPacket TryPinPacket, ServerPlayer serverPlayer, PacketSender packetSender) {
			AbstractContainerMenu container = serverPlayer.containerMenu;
			if(container.getType() == LocksContainerTypes.LOCK_PICKING)
				((LockPickingContainer) container).tryPin(TryPinPacket.pin);
		}
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeByte(this.pin);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public TryPinPacket(byte pin)
	{
		this.pin = pin;
	}

	public TryPinPacket(FriendlyByteBuf buf)
	{
		this(buf.readByte());
	}

}