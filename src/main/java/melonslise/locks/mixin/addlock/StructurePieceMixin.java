package melonslise.locks.mixin.addlock;

import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePiece.class)
public abstract class StructurePieceMixin {

//    @Inject(method = "placeBlock", at = @At(value = "TAIL"))
//    public void lockPlace(WorldGenLevel level, BlockState blockState, int x, int y, int z, BoundingBox boundingBox, CallbackInfo ci) {
//        LocksUtil.lockChunk(level, level.getLevel(), new BlockPos(x, y, z), RandomSource.create());
//    }
//
//    @Inject(method = "createChest(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "RETURN", ordinal = 0))
//    public void lockChest(ServerLevelAccessor levelAccessor, BoundingBox boundingBox, RandomSource randomSource, BlockPos blockPos, ResourceLocation resourceLocation, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
//        LocksUtil.lockChunk(levelAccessor, levelAccessor.getLevel(), blockPos, RandomSource.create());
//    }
//
//    @Inject(method = "createDispenser", at = @At(value = "RETURN", ordinal = 0))
//    public void lockDispenser(WorldGenLevel level, BoundingBox boundingBox, RandomSource randomSource, int x, int y, int z, Direction direction, ResourceLocation resourceLocation, CallbackInfoReturnable<Boolean> cir) {
//        LocksUtil.lockChunk(level, level.getLevel(), new BlockPos(x, y, z), RandomSource.create());
//    }
}
