package melonslise.locks.common.network.toclient;

import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RemoveLockablePacket implements FabricPacket {
    public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "remove_lockable");
    private final int id;

    public static final PacketType<RemoveLockablePacket> TYPE = PacketType.create(ID, RemoveLockablePacket::new);

    public static class Handler implements ClientPlayNetworking.PlayPacketHandler<RemoveLockablePacket>{
        @Override
        public void receive(RemoveLockablePacket pkt, LocalPlayer localPlayer, PacketSender packetSender) {
            LocksComponents.LOCKABLE_HANDLER.get(localPlayer.level()).remove(pkt.id);
        }
    }
    @Override
    public void write(FriendlyByteBuf buf) {

        buf.writeInt(this.id);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public RemoveLockablePacket(int id) {
        this.id = id;
    }

    public RemoveLockablePacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

}