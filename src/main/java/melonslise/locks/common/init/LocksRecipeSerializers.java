package melonslise.locks.common.init;

import melonslise.locks.Locks;
import melonslise.locks.common.recipe.KeyRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public final class LocksRecipeSerializers
{

	public static final RecipeSerializer<KeyRecipe> KEY = add("crafting_key", new SimpleCraftingRecipeSerializer<>(KeyRecipe::new));

	public static void register()
	{
	}

	public static <T extends Recipe<?>> RecipeSerializer<T> add(String name, RecipeSerializer<T> serializer)
	{
		return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,ResourceLocation.fromNamespaceAndPath(Locks.ID,name),serializer);
	}
}