package melonslise.locks.common.network.toclient;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ClientNet {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(AddLockablePacket.TYPE, AddLockablePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(AddLockableToChunkPacket.TYPE, AddLockableToChunkPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(RemoveLockablePacket.TYPE, RemoveLockablePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(TryPinResultPacket.TYPE, TryPinResultPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateLockablePacket.TYPE, UpdateLockablePacket.STREAM_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(AddLockablePacket.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                AddLockablePacket.handle(payload, context.player());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(AddLockableToChunkPacket.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                AddLockableToChunkPacket.handle(payload, context.player());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(RemoveLockablePacket.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                RemoveLockablePacket.handle(payload, context.player());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(TryPinResultPacket.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                TryPinResultPacket.handle(payload, context.player());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(UpdateLockablePacket.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                UpdateLockablePacket.handle(payload, context.player());
            });
        });
    }
}
