package melonslise.locks.common.config;

import com.google.common.collect.Lists;
import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public final class LocksConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue GENERATION_CHANCE;
    public static final ForgeConfigSpec.DoubleValue GENERATION_ENCHANT_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GENERATED_LOCKS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> GENERATED_LOCK_WEIGHTS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GEN_LOCKABLE_LOOT;

    public static final ForgeConfigSpec.BooleanValue RANDOMIZE_LOADED_LOCKS;

    public static NavigableMap<Integer, Item> weightedGeneratedLocks;
    public static int weightTotal;
    public static Pattern[] lockableGenBlocks;


    static {
        ForgeConfigSpec.Builder cfg = new ForgeConfigSpec.Builder();

        GENERATION_CHANCE = cfg
                .comment("Chance to generate a random lock on every new chest during world generation. Set to 0 to disable")
                .comment("在世界生成过程中，每个新箱子生成一个随机锁的概率。设置为0以禁用此功能。")
                .defineInRange("Generation Chance", 0.85d, 0d, 1d);
        GENERATION_ENCHANT_CHANCE = cfg
                .comment("Chance to randomly enchant a generated lock during world generation. Set to 0 to disable")
                .comment("在世界生成过程中，随机附魔生成的锁的概率。设置为0以禁用此功能。")
                .defineInRange("Generation Enchant Chance", 0.4d, 0d, 1d);
        GEN_LOCKABLE_LOOT = cfg
                .comment("Loot block that can be locked during the world generation")
                .comment("当世界生成时锁定的战利品方块")
                .defineList("Lockable Generated Blocks", Lists.newArrayList(".*chest", ".*barrel", "lootr:.*"), e -> e instanceof String);
        GENERATED_LOCKS = cfg
                .comment("Items that can be generated as locks (must be instance of LockItem in code!)")
                .comment("可以作为锁生成的物品（在代码中必须是 LockItem 的实例！）")
                .defineList("Generated Locks", Lists.newArrayList("locks:wood_lock", "locks:iron_lock", "locks:steel_lock", "locks:gold_lock", "locks:diamond_lock"), e -> e instanceof String);
        GENERATED_LOCK_WEIGHTS = cfg
                .comment("WARNING: THE AMOUNT OF NUMBERS SHOULD BE EQUAL TO THE AMOUNT OF GENERATED LOCK ITEMS!!!", "The relative probability that the corresponding lock item will be generated on a chest. Higher number = higher chance to generate")
                .comment("警告：数字的数量应与生成的锁数量相等！！！", "对应锁物品生成在箱子上的相对概率。数字越大，生成的机会越高。")
                .defineList("Generated Lock Chances", Lists.newArrayList(5, 4, 2, 3, 1), e -> e instanceof Integer);
        RANDOMIZE_LOADED_LOCKS = cfg
                .comment("Randomize lock IDs and combinations when loading them from a structure file. Randomization works just like during world generation")
                .comment("从结构文件加载锁ID和组合时进行随机化。随机化的方式与世界生成时相同。")
                .define("Randomize Loaded Locks", true);

        SPEC = cfg.build();
    }

    private LocksConfig() {
    }

    // https://gist.github.com/raws/1667807
    public static void init() {
        weightedGeneratedLocks = new TreeMap<>();
        weightTotal = 0;
        lockableGenBlocks = GEN_LOCKABLE_LOOT.get().stream().map(Pattern::compile).toArray(Pattern[]::new);
        List<? extends String> locks = GENERATED_LOCKS.get();
        List<? extends Integer> weights = GENERATED_LOCK_WEIGHTS.get();
        for (int a = 0; a < locks.size(); ++a) {
            weightTotal += weights.get(a);
            weightedGeneratedLocks.put(weightTotal, ForgeRegistries.ITEMS.getValue(new ResourceLocation(locks.get(a))));
        }
    }

    public static boolean canGen(RandomSource rng, Block block) {
        boolean random = LocksUtil.chance(rng, GENERATION_CHANCE.get());
        return random && matchString(block);
    }

    public static boolean matchString(Block block){
        String name = BuiltInRegistries.BLOCK.getKey(block).toString();
        for (Pattern p : lockableGenBlocks) {
            if (p.matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEnchant(RandomSource rng) {
        return LocksUtil.chance(rng, GENERATION_ENCHANT_CHANCE.get());
    }

    public static ItemStack getRandomLock(RandomSource rng) {
        ItemStack stack = new ItemStack(weightedGeneratedLocks.ceilingEntry(rng.nextInt(weightTotal) + 1).getValue());
        return canEnchant(rng) ? EnchantmentHelper.enchantItem(rng, stack, 5 + rng.nextInt(30), false) : stack;
    }
}