package melonslise.locks.mixin;

import melonslise.locks.common.components.interfaces.ILockableHandler;
import melonslise.locks.common.components.interfaces.ILockableStorage;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.network.toclient.AddLockableToChunkPacket;
import melonslise.locks.common.util.ILockableProvider;
import melonslise.locks.common.util.Lockable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public class ChunkMixin
{
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ProtoChunk;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;)V")
	private void init(ServerLevel world, ProtoChunk pChunk, LevelChunk.PostLoadProcessor pPostLoad, CallbackInfo ci)
	{
		LevelChunk ch = (LevelChunk) (Object) this;
		ILockableStorage st = LocksComponents.LOCKABLE_STORAGE.get(ch);
		ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(world);
		// We trust that all checks pass (such as volume and intersect checks) due to this happening only during world gen
		for(Lockable lkb : ((ILockableProvider) pChunk).getLockables())
		{
			if(lkb.tr==null){
				continue;
			}

			st.add(lkb);
			handler.getLoaded().put(lkb.id, lkb);
			lkb.addObserver(handler);
//			AddLockableToChunkPacket.execute(new AddLockableToChunkPacket(lkb, ch), world);
			world.getServer().getPlayerList().players.forEach(player -> {
				ServerPlayNetworking.send(player,new AddLockableToChunkPacket(lkb, ch));
			});
		}
	}
}