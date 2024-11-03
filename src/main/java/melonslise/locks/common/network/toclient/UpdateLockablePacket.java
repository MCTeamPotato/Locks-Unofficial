package melonslise.locks.common.network.toclient;

import melonslise.locks.Locks;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class UpdateLockablePacket implements FabricPacket {
    public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "update_lockable");
    private final int id;
    // Expandable
    private final boolean locked;

    public static final PacketType<UpdateLockablePacket> TYPE = PacketType.create(ID, UpdateLockablePacket::new);

    public static class Handler implements ClientPlayNetworking.PlayPacketHandler<UpdateLockablePacket>{
        @Override
        public void receive(UpdateLockablePacket pkt, LocalPlayer localPlayer, PacketSender packetSender) {
            LocksComponents.LOCKABLE_HANDLER.get(localPlayer.level()).getLoaded().get(pkt.id).lock.setLocked(pkt.locked);
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.id);
        friendlyByteBuf.writeBoolean(this.locked);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public UpdateLockablePacket(int id, boolean locked) {
        this.id = id;
        this.locked = locked;
    }

    public UpdateLockablePacket(Lockable lkb) {
        this(lkb.id, lkb.lock.isLocked());
    }

    public UpdateLockablePacket(FriendlyByteBuf buf) {
          this(buf.readInt(), buf.readBoolean());
    }

}