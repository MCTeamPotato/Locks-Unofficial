package melonslise.locks.common.components.interfaces;

import org.ladysnake.cca.api.v3.component.TransientComponent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IItemHandler extends TransientComponent, AutoSyncedComponent, Container {
        List<ItemStack> getItems();
        void setItems(List<ItemStack> items);
        int getSlots();
        ItemStack getStackInSlot(int slot);
}
