package melonslise.locks.mixin.accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootTable.class)
public interface LootTableAccessor
{
	@Accessor("pools")
	LootPool[] getPools();

	@Accessor("pools")
	void setPools(LootPool[] pools);
}