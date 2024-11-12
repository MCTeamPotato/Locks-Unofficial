package melonslise.locks.mixin;

import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public class PistonBlockMixin
{
	// Before getPistonPushReaction call
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"), method = "isPushable", cancellable = true)
	private static void isPushable(BlockState state, Level world, BlockPos pos, Direction dir, boolean flag, Direction dir1, CallbackInfoReturnable<Boolean> cir)
	{
		if(LocksUtil.lockedAndRelated(world, pos))
			cir.setReturnValue(false);
	}
}