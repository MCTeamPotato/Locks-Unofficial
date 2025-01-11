package melonslise.locks.common.network.toclient;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ClientNet {

    public static void register() {

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
