package melonslise.locks.common.worldgen.feature;

import com.mojang.serialization.Codec;
import melonslise.locks.common.config.LocksConfig;
import melonslise.locks.common.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;
import static net.minecraft.world.level.block.state.properties.DoorHingeSide.LEFT;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;

    public class LocksFeature extends Feature<NoneFeatureConfiguration> {
    public LocksFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldGenLevel = context.level();
        BlockPos blockPos = context.origin();
        RandomSource randomSource = context.random();
        BlockState state = worldGenLevel.getBlockState(blockPos);
        Block block = state.getBlock();
        if (LocksConfig.canGen(randomSource, block)) {
            BlockPos pos1 = blockPos;
            Direction dir;
            if (state.hasProperty(FACING)){
                dir = state.getValue(FACING);
            } else if(state.hasProperty(HORIZONTAL_FACING)){
                dir = state.getValue(HORIZONTAL_FACING);
            } else {
                dir = Direction.NORTH;
            }

            if (state.hasProperty(CHEST_TYPE)) {
                switch (state.getValue(CHEST_TYPE)) {
                    case LEFT -> pos1 = blockPos.relative(ChestBlock.getConnectedDirection(state));
                    case RIGHT -> {
                        return false;
                    }
                }
            }
            if (state.hasProperty(DOUBLE_BLOCK_HALF)) {
                if (state.getValue(DOUBLE_BLOCK_HALF) == LOWER) return false;
                pos1 = blockPos.below();
                if (state.hasProperty(DOOR_HINGE)) {
                    if (state.hasProperty(DOOR_HINGE) && state.hasProperty(HORIZONTAL_FACING)) {
                        BlockPos pos2 = pos1.relative(state.getValue(DOOR_HINGE) == LEFT ? dir.getClockWise() : dir.getCounterClockWise());
                        if (worldGenLevel.getBlockState(pos2).is(state.getBlock())) {
                            if (state.getValue(DOOR_HINGE) == LEFT) {
                                return false;
                            }
                            pos1 = pos2;
                        }
                    }
                    dir = dir.getOpposite();
                }
            }
            Cuboid6i bb = new Cuboid6i(blockPos, pos1);
            ItemStack stack = LocksConfig.getRandomLock(randomSource);
            Lock lock = Lock.from(stack);
            Transform tr = Transform.fromDirection(dir, dir);
            Lockable lkb = new Lockable(bb, lock, tr, stack, worldGenLevel.getLevel());
            lkb.bb.getContainedChunks((x, z) -> {
                ((ILockableProvider) worldGenLevel.getChunk(x, z)).getLockables().add(lkb);
                return true;
            });
        }
        return true;
    }


    public static class Config implements FeatureConfiguration {
        /*
        public static final Codec<NoneFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("max_height").orElse(64).forGetter(null)
        ).apply(instance, Config::new));
        public static final NoneFeatureConfiguration INSTANCE = new NoneFeatureConfiguration();
         */
    }
}
