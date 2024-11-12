package melonslise.locks.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.Builder.class)
public interface LootPoolAccessor
{
	@Accessor
	ImmutableList.Builder<LootPoolEntryContainer> getEntries();
}