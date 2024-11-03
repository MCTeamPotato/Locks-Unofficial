package melonslise.locks.mixin;

import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.network.toclient.AddLockableToChunkPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkManagerMixin
{
	@Inject(at = @At("TAIL"), method = "playerLoadedChunk")
	private void playerLoadedChunk(ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> pkts, LevelChunk ch, CallbackInfo ci)
	{
		LocksComponents.LOCKABLE_STORAGE.get(ch).get().values()
			.forEach(lkb -> ServerPlayNetworking.send(player, new AddLockableToChunkPacket(lkb, ch)));
//		LocksComponents.LOCKABLE_STORAGE.get(ch).get().values()
//				.forEach(lkb -> AddLockableToChunkPacket.execute(new AddLockableToChunkPacket(lkb, ch), player.level()));
	}
}