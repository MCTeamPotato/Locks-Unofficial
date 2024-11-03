package melonslise.locks.mixin;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnchantmentCategory.class)
public class EnchantmentCategoryMixin {

    @Shadow
    @Final
    @Mutable
    private static EnchantmentCategory[] $VALUES;

}
