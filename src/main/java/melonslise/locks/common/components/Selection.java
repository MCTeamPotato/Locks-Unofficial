package melonslise.locks.common.components;

import melonslise.locks.Locks;
import melonslise.locks.common.components.interfaces.ISelection;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class Selection implements ISelection{

    public static final ResourceLocation ID = new ResourceLocation(Locks.ID, "selection");

    public BlockPos pos;

    public Selection(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public BlockPos get()
    {
        return this.pos;
    }

    @Override
    public void set(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        this.pos = new BlockPos(compoundTag.getInt("pos_x"), compoundTag.getInt("pos_y"), compoundTag.getInt("pos_z"));
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        if(this.pos == null) {
            pos = new BlockPos(0, 0, 0);
        }
        compoundTag.putInt("pos_x", this.pos.getX());
        compoundTag.putInt("pos_y", this.pos.getY());
        compoundTag.putInt("pos_z", this.pos.getZ());
    }
}
