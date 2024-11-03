package melonslise.locks.common.network.toclient;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNet {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(AddLockablePacket.TYPE,new AddLockablePacket.Handler());
        ClientPlayNetworking.registerGlobalReceiver(AddLockableToChunkPacket.TYPE, new AddLockableToChunkPacket.Handler());
        ClientPlayNetworking.registerGlobalReceiver(RemoveLockablePacket.TYPE, new RemoveLockablePacket.Handler());
        ClientPlayNetworking.registerGlobalReceiver(TryPinResultPacket.TYPE, new TryPinResultPacket.Handler());
        ClientPlayNetworking.registerGlobalReceiver(UpdateLockablePacket.TYPE, new UpdateLockablePacket.Handler());
    }
}
