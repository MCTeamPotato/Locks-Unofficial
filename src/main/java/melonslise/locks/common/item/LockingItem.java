package melonslise.locks.common.item;

import melonslise.locks.Locks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LockingItem extends Item
{
	public LockingItem(Properties props)
	{
		super(props.stacksTo(1));
	}

	public static final String KEY_ID = "Id";

	public static ItemStack copyId(ItemStack from, ItemStack to)
	{
		int copiedId = getOrSetId(from);

		to.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentNbt ->
			currentNbt.putInt(KEY_ID, copiedId)
		));

		return to;
	}

	public static int getOrSetId(ItemStack stack)
	{
		@Nullable var data = stack.get(DataComponents.CUSTOM_DATA);

		if (data == null || !data.contains(KEY_ID)){
			CompoundTag idTag = new CompoundTag();
			int randId = ThreadLocalRandom.current().nextInt();
			idTag.putInt(KEY_ID, randId);
			CustomData customData = CustomData.of(idTag);
			stack.set(DataComponents.CUSTOM_DATA, customData);
			return randId;
		}

		return data.copyTag().getInt(KEY_ID);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected)
	{
		if(!world.isClientSide)
			getOrSetId(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> lines, TooltipFlag flag)
	{
		CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if(compoundTag.contains(KEY_ID))
			lines.add(Component.translatable(Locks.ID + ".tooltip.id", getOrSetId(stack)).withStyle(ChatFormatting.DARK_GREEN));
	}
}