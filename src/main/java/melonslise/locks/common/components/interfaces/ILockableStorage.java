package melonslise.locks.common.components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import melonslise.locks.common.util.Lockable;

public interface ILockableStorage extends Component, AutoSyncedComponent {
    Int2ObjectMap<Lockable> get();

    void add(Lockable lkb);

    void remove(int id);
}
