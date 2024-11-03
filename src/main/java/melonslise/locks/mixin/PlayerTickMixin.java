package melonslise.locks.mixin;


import melonslise.locks.common.components.interfaces.ISelection;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.init.LocksItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {

        ISelection select = LocksComponents.SELECTION.get((Player) (Object) this);
        if (select == null)
            return;
        for (ItemStack stack : ((Player) (Object) this).getHandSlots())
            if(stack.is(LocksItemTags.LOCKS))
                return;
        select.set(null);
    }
}
