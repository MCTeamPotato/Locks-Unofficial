package melonslise.locks.common.config;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.regex.Pattern;

public class LocksServerConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue MAX_LOCKABLE_VOLUME;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LOCKABLE_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue ALLOW_REMOVING_LOCKS;
    public static final ForgeConfigSpec.BooleanValue PROTECT_LOCKABLES;
    public static final ForgeConfigSpec.BooleanValue EASY_LOCK;
    public static final ForgeConfigSpec.BooleanValue STRONG_PREVENTION;

    public static Pattern[] lockableBlocks;

    static {
        ForgeConfigSpec.Builder cfg = new ForgeConfigSpec.Builder();

        MAX_LOCKABLE_VOLUME = cfg
                .comment("Maximum amount of blocks that can be locked at once")
                .comment("一次最多可以锁定的方块数量")
                .defineInRange("Max Lockable Volume", 6, 1, Integer.MAX_VALUE);
        LOCKABLE_BLOCKS = cfg
                .comment("Blocks that can be locked. Each entry is the mod domain followed by the block's registry name. Can include regular expressions")
                .comment("可以锁定的方块。每个值由模组域名和块的注册名称组成，可以包含正则表达式。")
                .defineList("Lockable Blocks", Lists.newArrayList(".*chest", ".*barrel", ".*hopper", ".*door", ".*trapdoor", ".*fence_gate", ".*shulker_box"), e -> e instanceof String);
        ALLOW_REMOVING_LOCKS = cfg
                .comment("Open locks can be removed with an empty hand while sneaking")
                .comment("在潜行状态下，可以用空手移除打开的锁。")
                .define("Allow Removing Locks", true);
        PROTECT_LOCKABLES = cfg
                .comment("Locked blocks cannot be destroyed in survival mode")
                .comment("在生存模式中，锁定的块无法被破坏。")
                .define("Protect Lockables", true);
        EASY_LOCK = cfg
                .comment("Lock blocks with just one click! It's magic! (Cancel will probably fail spectacularly with custom doors, custom double chests, etc)")
                .comment("只需点击一下即可锁定块！这太神奇了！ （取消可能会使自定义门、自定义双箱等出现严重错误）")
                .define("Easy Lock", true);
        STRONG_PREVENTION = cfg
                .comment("Use stronger checks to prevent blocks on locks from being broken, but its compatibility is unknown")
                .comment("使用更强的检查来防止上锁方块被破坏，兼容性未知")
                .define("Stronger Prevention", false);
        SPEC = cfg.build();
    }

    private LocksServerConfig() {
    }

    public static void init() {
        lockableBlocks = LOCKABLE_BLOCKS.get().stream().map(Pattern::compile).toArray(Pattern[]::new);
    }

    public static boolean canLock(Level world, BlockPos pos) {
        if(lockableBlocks==null){
            init();
        }
        Block block = world.getBlockState(pos).getBlock();
        String name = BuiltInRegistries.BLOCK.getKey(block).toString();
        for (Pattern p : lockableBlocks) {
            if (p.matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }
}