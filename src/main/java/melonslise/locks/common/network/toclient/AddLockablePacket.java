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

public class AddLockablePacket implements FabricPacket  {
    public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "add_lockable");
    public static final PacketType<AddLockablePacket> TYPE = PacketType.create(ID, AddLockablePacket::new);

    public static class Handler implements ClientPlayNetworking.PlayPacketHandler<AddLockablePacket>{
        @Override
        public void receive(AddLockablePacket pkt, LocalPlayer localPlayer, PacketSender packetSender) {
            LocksComponents.LOCKABLE_HANDLER.get(localPlayer.level()).add(pkt.lockable,localPlayer.level());
        }
    }

    private final Lockable lockable;

    public AddLockablePacket(Lockable lkb) {
        this.lockable = lkb;
    }

    public AddLockablePacket(FriendlyByteBuf buf) {
        this(Lockable.fromBuf(buf));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        Lockable.toBuf(friendlyByteBuf, this.lockable);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

}