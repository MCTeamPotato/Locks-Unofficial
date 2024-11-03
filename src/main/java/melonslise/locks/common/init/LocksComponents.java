package melonslise.locks.common.init;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import melonslise.locks.Locks;
import melonslise.locks.common.components.ItemHandler;
import melonslise.locks.common.components.LockableHandler;
import melonslise.locks.common.components.LockableStorage;
import melonslise.locks.common.components.Selection;
import melonslise.locks.common.components.interfaces.IItemHandler;
import melonslise.locks.common.components.interfaces.ILockableHandler;
import melonslise.locks.common.components.interfaces.ILockableStorage;
import melonslise.locks.common.components.interfaces.ISelection;
import melonslise.locks.common.item.LockItem;
import net.minecraft.resources.ResourceLocation;

public class LocksComponents implements EntityComponentInitializer, WorldComponentInitializer, ChunkComponentInitializer, ItemComponentInitializer {

    public static final ComponentKey<ILockableHandler> LOCKABLE_HANDLER =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"lockable_handler"),ILockableHandler.class);

    public static final ComponentKey<ILockableStorage> LOCKABLE_STORAGE =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"lockable_storage"), ILockableStorage.class);

    public static final ComponentKey<ISelection> SELECTION =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"selection"), ISelection.class);

    public static final ComponentKey<IItemHandler> ITEM_HANDLER =
            ComponentRegistry.getOrCreate(new ResourceLocation(Locks.ID,"item_handler"), IItemHandler.class);

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry chunkComponentFactoryRegistry) {
        chunkComponentFactoryRegistry.register(LOCKABLE_STORAGE, LockableStorage::new);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(SELECTION, (player) -> new Selection(player.getOnPos()), RespawnCopyStrategy.ALWAYS_COPY);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry worldComponentFactoryRegistry) {
        worldComponentFactoryRegistry.register(LOCKABLE_HANDLER, LockableHandler::new);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry itemComponentFactoryRegistry) {
        itemComponentFactoryRegistry.registerTransient(item -> item instanceof LockItem,ITEM_HANDLER, (stack) -> new ItemHandler());
        itemComponentFactoryRegistry.registerTransient(LocksItems.KEY_RING,ITEM_HANDLER, (stack) -> new ItemHandler());
    }
}
