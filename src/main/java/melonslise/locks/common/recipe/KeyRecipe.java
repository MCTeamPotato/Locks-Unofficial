package melonslise.locks.common.recipe;

import melonslise.locks.common.init.LocksItems;
import melonslise.locks.common.init.LocksRecipeSerializers;
import melonslise.locks.common.item.LockingItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class KeyRecipe extends CustomRecipe
{
	public KeyRecipe(CraftingBookCategory pCategory)
	{
		super(pCategory);
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return LocksRecipeSerializers.KEY;
	}

	@Override
	public boolean matches(CraftingInput recipeInput, Level level) {
		boolean hasLocking = false;
		int blanks = 0;

		for(int a = 0; a < recipeInput.size(); ++a)
		{
			ItemStack stack = recipeInput.getItem(a);
			if(stack.isEmpty())
				continue;
			if(stack.getItem() instanceof LockingItem && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(LockingItem.KEY_ID))
			{
				if(hasLocking)
					return false;
				hasLocking = true;
			}
			else if(stack.getItem() == LocksItems.KEY_BLANK)
				++blanks;
			else
				return false;
		}
		return hasLocking && blanks >= 1;
	}
	@Override
	public ItemStack assemble(CraftingInput recipeInput, HolderLookup.Provider provider) {
		ItemStack locking = ItemStack.EMPTY;
		int blanks = 0;

		for (int a = 0; a < recipeInput.size(); ++a)
		{
			ItemStack stack = recipeInput.getItem(a);
			if (stack.isEmpty())
				continue;
			if (stack.getItem() instanceof LockingItem && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(LockingItem.KEY_ID))
			{
				if (!locking.isEmpty())
					return ItemStack.EMPTY;
				locking = stack;
			}
			else if(stack.getItem() == LocksItems.KEY_BLANK)
				++blanks;
			else
				return ItemStack.EMPTY;
		}

		if(!locking.isEmpty() && blanks >= 1)
			return LockingItem.copyId(locking, new ItemStack(LocksItems.KEY, blanks));
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inv)
	{
		NonNullList<ItemStack> list = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for (int a = 0; a < list.size(); ++a)
		{
			ItemStack stack = inv.getItem(a);
			if(!(stack.getItem() instanceof LockingItem  && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(LockingItem.KEY_ID)))
				continue;
			list.set(a, stack.copy());
			break;
		}

		return list;
	}



	@Override
	public boolean canCraftInDimensions(int x, int y)
	{
		return x >= 3 && y >= 3;
	}
}