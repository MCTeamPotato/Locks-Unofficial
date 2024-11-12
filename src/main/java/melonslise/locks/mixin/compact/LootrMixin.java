package melonslise.locks.mixin.compact;

import melonslise.locks.Locks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "noobanidus.mods.lootr.common.block.entity.BlockEntityTicker")
public class LootrMixin {
    @Redirect(method = "onServerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
    private static boolean lockLootr(ServerLevel instance, BlockPos pos, boolean b){
        //Locks.LOGGER.info(instance.getBlockState(pos));
        return b;
    }
}
