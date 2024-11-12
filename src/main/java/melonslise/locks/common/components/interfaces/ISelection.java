package melonslise.locks.common.components.interfaces;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;
import net.minecraft.core.BlockPos;

public interface ISelection extends RespawnableComponent, AutoSyncedComponent {
    BlockPos get();

    void set(BlockPos pos);
}
