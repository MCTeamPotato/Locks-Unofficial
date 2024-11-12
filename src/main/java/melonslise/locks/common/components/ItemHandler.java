package melonslise.locks.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import melonslise.locks.common.components.interfaces.IItemHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;

public record ItemHandler(List<ItemStack> items) implements IItemHandler {


    public static final Codec<ItemHandler> CODEC = RecordCodecBuilder.create(itemHandlerInstance ->
            itemHandlerInstance.group(
                    Codec.list(ItemStack.CODEC).fieldOf("items").forGetter(ItemHandler::items)
            ).apply(itemHandlerInstance, ItemHandler::new)
    );
    public static final StreamCodec<ByteBuf,ItemHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Codec.list(ItemStack.CODEC)),ItemHandler::items,
            ItemHandler::new
    );

    public ItemHandler() {
        this(NonNullList.withSize(0, ItemStack.EMPTY));
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.items.clear();
        int i = compoundTag.getInt("size");
        this.items.addAll(NonNullList.withSize(i, ItemStack.EMPTY));
        ContainerHelper.loadAllItems(compoundTag, this.items,provider);
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putInt("size", this.items.size());
        ContainerHelper.saveAllItems(compoundTag, this.items,provider);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = this.items.get(i);
        itemStack.setCount(itemStack.getCount() - j);
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        this.items.set(i, ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.items.set(i, itemStack);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public List<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void setItems(List<ItemStack> items) {
        this.items = NonNullList.withSize(items.size(), ItemStack.EMPTY);
        Iterator<ItemStack> iterator = items.iterator();
        for (int i = 0; i < items.size(); i++) {
            this.items.set(i, iterator.next());
        }
    }

    @Override
    public int getSlots() {
        return this.items.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.items.get(slot);
    }
}
