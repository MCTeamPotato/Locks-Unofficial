package melonslise.locks.mixin;

import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperTileEntityMixin
{
//	@Inject(at = @At("HEAD"), method = "getContainerAt(Lnet/minecraft/world/level/Level;DDD)Lnet/minecraft/world/Container;", cancellable = true)
//	private static void getContainerAt(Level world, double x, double y, double z, CallbackInfoReturnable<Inventory> cir)
//	{
//		if(LocksUtil.locked(world, new BlockPos((int) x, (int) y, (int) z)))
//			cir.setReturnValue(null);
//	}
}