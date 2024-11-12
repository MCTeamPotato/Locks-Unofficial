package melonslise.locks.mixin;

import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.network.toclient.AddLockableToChunkPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(ChunkMap.class)
public class ChunkManagerMixin
{
	@Inject(at = @At("TAIL"), method = "onChunkReadyToSend(Lnet/minecraft/world/level/chunk/LevelChunk;)V")
	private void playerLoadedChunk(LevelChunk levelChunk, CallbackInfo ci)
	{
		ChunkPos chunkPos = levelChunk.getPos();
		Iterator<ServerPlayer> var3 = ((ChunkMap)(Object)this).playerMap.getAllPlayers().iterator();
		var3.forEachRemaining(player ->
				LocksComponents.LOCKABLE_STORAGE.get(levelChunk).get().values()
				.forEach(lkb -> ServerPlayNetworking.send(player, new AddLockableToChunkPacket(lkb, levelChunk)))
		);

//		LocksComponents.LOCKABLE_STORAGE.get(ch).get().values()
//				.forEach(lkb -> AddLockableToChunkPacket.execute(new AddLockableToChunkPacket(lkb, ch), player.level()));
	}
}