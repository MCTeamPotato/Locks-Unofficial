package melonslise.locks.common.network.toclient;

import melonslise.locks.Locks;
import melonslise.locks.common.container.LockPickingContainer;
import melonslise.locks.common.init.LocksContainerTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class TryPinResultPacket implements FabricPacket {
	public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "try_pin_result");
	public final boolean correct, reset;

	public static final PacketType<TryPinResultPacket> TYPE = PacketType.create(ID, TryPinResultPacket::new);

	public static class Handler implements ClientPlayNetworking.PlayPacketHandler<TryPinResultPacket>{
		@Override
		public void receive(TryPinResultPacket pkt, LocalPlayer localPlayer, PacketSender packetSender) {
			AbstractContainerMenu container = localPlayer.containerMenu;
			if(container.getType() == LocksContainerTypes.LOCK_PICKING)
				((LockPickingContainer) container).handlePin(pkt.correct, pkt.reset);
		}
	}

	@Override
	public void write(FriendlyByteBuf buf) {

		buf.writeBoolean(this.correct);
		buf.writeBoolean(this.reset);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	
	public TryPinResultPacket(boolean correct, boolean reset)
	{
		this.correct = correct;
		this.reset = reset;
	}

	public TryPinResultPacket(FriendlyByteBuf buf)
	{
		this(buf.readBoolean(), buf.readBoolean());
	}



}