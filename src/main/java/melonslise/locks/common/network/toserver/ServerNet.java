package melonslise.locks.common.network.toserver;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNet {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(TryPinPacket.TYPE,new TryPinPacket.Handler());

    }
}
