package melonslise.locks.mixin;

import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
public class PistonBlockStructureHelperMixin
{
	@Shadow @Final private Level level;

	@Shadow @Final private BlockPos startPos;

	@Inject(at = @At("HEAD"), method = "resolve()Z", cancellable = true)
	private void resolve(CallbackInfoReturnable<Boolean> cir)
	{
		if(LocksUtil.lockedAndRelated(this.level, this.startPos))
			cir.setReturnValue(false);
	}
}