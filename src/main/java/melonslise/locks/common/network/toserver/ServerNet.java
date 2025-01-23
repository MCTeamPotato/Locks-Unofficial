package melonslise.locks.common.network.toserver;

import melonslise.locks.common.network.toclient.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNet {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(AddLockablePacket.TYPE, AddLockablePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(AddLockableToChunkPacket.TYPE, AddLockableToChunkPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RemoveLockablePacket.TYPE, RemoveLockablePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(TryPinResultPacket.TYPE, TryPinResultPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateLockablePacket.TYPE, UpdateLockablePacket.STREAM_CODEC);

        PayloadTypeRegistry.playC2S().register(TryPinPacket.TYPE, TryPinPacket.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TryPinPacket.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                TryPinPacket.handle(payload, context.player());
            });
        });
    }
}
