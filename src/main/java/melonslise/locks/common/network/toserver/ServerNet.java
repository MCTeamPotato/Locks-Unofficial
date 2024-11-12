package melonslise.locks.common.network.toserver;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNet {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(TryPinPacket.TYPE, TryPinPacket.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TryPinPacket.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                TryPinPacket.handle(payload, context.player());
            });
        });
    }
}
