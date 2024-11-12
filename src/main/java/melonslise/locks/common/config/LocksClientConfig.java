package melonslise.locks.common.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public final class LocksClientConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue DEAF_MODE;
    public static final ModConfigSpec.BooleanValue OVERLAY;

    private LocksClientConfig() {
    }

    static {
        ModConfigSpec.Builder cfg = new ModConfigSpec.Builder();

        DEAF_MODE = cfg
                .comment("Display visual feedback when trying to use a locked block for certain hearing impaired individuals")
                .comment("在尝试交互锁定方块时为某些听力障碍人士提供视觉反馈。")
                .define("Deaf Mode", true);

        OVERLAY = cfg
                .comment("Whether or not to display the lock's information overlay when holding the lock picker")
                .comment("是否在持有撬锁器时显示锁的信息覆盖层")
                .define("Show Overlay", true);

        SPEC = cfg.build();
    }
}