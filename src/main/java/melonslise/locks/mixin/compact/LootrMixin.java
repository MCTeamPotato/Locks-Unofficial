package melonslise.locks.mixin.compact;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.zestyblaze.lootr.block.entities.TileTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = TileTicker.class)
public class LootrMixin {
    @Redirect(method = "serverTick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private static boolean lockLootr(ServerLevel instance, BlockPos pos, boolean b){
        return b;
    }
}
