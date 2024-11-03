package melonslise.locks.mixin;

import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public class DoorMixin {
    @Inject(method = "setOpen", at = @At(value = "HEAD"), cancellable = true)
    private void cancelOpen(Entity pEntity, Level pLevel, BlockState pState, BlockPos pPos, boolean pOpen, CallbackInfo ci){
        if (LocksUtil.locked(pLevel, pPos)){
            ci.cancel();
        }
    }
}
