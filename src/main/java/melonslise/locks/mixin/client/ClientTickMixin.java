package melonslise.locks.mixin.client;

import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ClientTickMixin {

    @Shadow
    public ClientLevel level;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if(level==null){
            return;
        }
        LocksComponents.LOCKABLE_HANDLER.get(level).getLoaded().values().forEach(Lockable::tick);
    }
}
