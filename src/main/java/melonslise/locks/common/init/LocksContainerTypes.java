package melonslise.locks.common.init;

import com.mojang.serialization.Codec;
import melonslise.locks.Locks;
import melonslise.locks.common.container.KeyRingContainer;
import melonslise.locks.common.container.LockPickingContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.List;

public final class LocksContainerTypes
{


	public static final ExtendedScreenHandlerType<LockPickingContainer, List<Integer>>
			LOCK_PICKING = new ExtendedScreenHandlerType<>(LockPickingContainer.FACTORY,ByteBufCodecs.fromCodec(Codec.list(Codec.INT)));

	public static final ExtendedScreenHandlerType<KeyRingContainer, Integer>
			KEY_RING = new ExtendedScreenHandlerType<>(KeyRingContainer.FACTORY, ByteBufCodecs.INT);

	public static final MenuType<LockPickingContainer>
			LOCK_PICKING_TYPE = add("lock_picking", LOCK_PICKING);

	public static final MenuType<KeyRingContainer>
			KEY_RING_TYPE= add("key_ring", KEY_RING);

	private LocksContainerTypes() {}

	public static void register()
	{
	}

	public static <T extends AbstractContainerMenu> MenuType<T> add(String name, MenuType<T> type)
	{
		return Registry.register(BuiltInRegistries.MENU,ResourceLocation.fromNamespaceAndPath(Locks.ID,name),type);
	}
}