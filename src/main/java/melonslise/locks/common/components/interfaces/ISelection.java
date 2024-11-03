package melonslise.locks.common.components.interfaces;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.core.BlockPos;

public interface ISelection extends PlayerComponent, AutoSyncedComponent {
    BlockPos get();

    void set(BlockPos pos);
}
