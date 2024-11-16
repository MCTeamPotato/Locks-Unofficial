package melonslise.locks.mixin;


import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("DataFlowIssue")
@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {

    @Inject(method = "getItem(I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN"), cancellable = true)
    private void lockRandomizableContainerBlockEntity(int slot, CallbackInfoReturnable<ItemStack> cir) {
        BlockPos pos = ((BaseContainerBlockEntity) (Object) this).getBlockPos();
        Level level = ((BaseContainerBlockEntity) (Object) this).getLevel();
        if (level.isClientSide) return;
        if (LocksUtil.locked(level, pos)){
            cir.setReturnValue(Items.AIR.getDefaultInstance());
        }
    }
}
