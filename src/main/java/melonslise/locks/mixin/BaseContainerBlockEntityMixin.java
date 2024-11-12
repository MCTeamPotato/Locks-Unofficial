package melonslise.locks.mixin;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import melonslise.locks.common.components.interfaces.ILockableHandler;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.util.Lockable;
import melonslise.locks.common.util.LocksPredicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@SuppressWarnings("DataFlowIssue")
@Mixin(BaseContainerBlockEntity.class)
public class BaseContainerBlockEntityMixin {

    @ModifyReturnValue(method = "getItem(I)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private ItemStack lockBaseContainerBlockEntity(ItemStack original) {
        BlockPos pos = ((BaseContainerBlockEntity) (Object) this).getBlockPos();
        Level level = ((BaseContainerBlockEntity) (Object) this).getLevel();
        ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(level);
        Lockable[] intersect = handler.getInChunk(pos).values().stream().filter(lkb -> lkb.bb.intersects(pos)).toArray(Lockable[]::new);
        Optional<Lockable> locked = Arrays.stream(intersect).filter(LocksPredicates.LOCKED).findFirst();
        if(locked.isPresent())
        {
            return ItemStack.EMPTY;
        }
        return original;
    }

    @ModifyReturnValue(method = "removeItem(II)Lnet/minecraft/world/item/ItemStack;",at = @At("RETURN"))
    private ItemStack lockBaseContainerBlockEntityRemoveItem(ItemStack original) {
        BlockPos pos = ((BaseContainerBlockEntity) (Object) this).getBlockPos();
        Level level = ((BaseContainerBlockEntity) (Object) this).getLevel();
        ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(level);
        Lockable[] intersect = handler.getInChunk(pos).values().stream().filter(lkb -> lkb.bb.intersects(pos)).toArray(Lockable[]::new);
        Optional<Lockable> locked = Arrays.stream(intersect).filter(LocksPredicates.LOCKED).findFirst();
        if(locked.isPresent())
        {
            return ItemStack.EMPTY;
        }
        return original;
    }
    @Redirect(method = "removeItem(II)Lnet/minecraft/world/item/ItemStack;",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ContainerHelper;removeItem(Ljava/util/List;II)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack lockBaseContainerBlockEntityRemoveItem(List<ItemStack> list, int i, int j) {
        BlockPos pos = ((BaseContainerBlockEntity) (Object) this).getBlockPos();
        Level level = ((BaseContainerBlockEntity) (Object) this).getLevel();
        ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(level);
        Lockable[] intersect = handler.getInChunk(pos).values().stream().filter(lkb -> lkb.bb.intersects(pos)).toArray(Lockable[]::new);
        Optional<Lockable> locked = Arrays.stream(intersect).filter(LocksPredicates.LOCKED).findFirst();
        if(locked.isPresent())
        {
            return ItemStack.EMPTY;
        }
        return ContainerHelper.removeItem(list, i, j);
    }
    @Redirect(method = "setItem(ILnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BaseContainerBlockEntity;getItems()Lnet/minecraft/core/NonNullList;"))
    private NonNullList<ItemStack> lockBaseContainerBlockEntitySetItem(BaseContainerBlockEntity instance) {
        BlockPos pos = ((BaseContainerBlockEntity) (Object) this).getBlockPos();
        Level level = ((BaseContainerBlockEntity) (Object) this).getLevel();
        ILockableHandler handler = LocksComponents.LOCKABLE_HANDLER.get(level);
        Lockable[] intersect = handler.getInChunk(pos).values().stream().filter(lkb -> lkb.bb.intersects(pos)).toArray(Lockable[]::new);
        Optional<Lockable> locked = Arrays.stream(intersect).filter(LocksPredicates.LOCKED).findFirst();
        if(locked.isPresent())
        {
            return NonNullList.withSize(instance.getContainerSize(), ItemStack.EMPTY);
        }
        return instance.getItems();
    }

}
