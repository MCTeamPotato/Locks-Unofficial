package melonslise.locks.common.item;

import melonslise.locks.common.components.ItemHandler;
import melonslise.locks.common.components.interfaces.IItemHandler;
import melonslise.locks.common.container.KeyRingContainer;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.init.LocksSoundEvents;
import melonslise.locks.common.util.Lockable;
import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public class KeyRingItem extends Item
{
	public final int rows;

	public KeyRingItem(int rows, Properties props)
	{
		super(props.stacksTo(1));
		this.rows = rows;
	}

	public static boolean containsId(ItemStack stack, int id)
	{
		ItemHandler inv = stack.get(LocksComponents.ITEM_HANDLER);
		for(int a = 0; a < inv.getSlots(); ++a)
			if(LockingItem.getOrSetId(inv.getStackInSlot(a)) == id)
				return true;
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(player instanceof ServerPlayer serverPlayer){
			serverPlayer.openMenu(new KeyRingContainer.Provider(stack));
		}
//			NetworkHooks.openScreen((ServerPlayer) player, new KeyRingContainer.Provider(stack), new KeyRingContainer.Writer(hand));
		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx)
	{
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		ItemHandler inv = ctx.getItemInHand().get(LocksComponents.ITEM_HANDLER);
		List<Lockable> intersect = LocksUtil.intersecting(world, pos).collect(Collectors.toList());
		if(intersect.isEmpty())
			return InteractionResult.PASS;
		for(int a = 0; a < inv.getSlots(); ++a)
		{
			int id = LockingItem.getOrSetId(inv.getStackInSlot(a));
			List<Lockable> match = intersect.stream().filter(lkb -> lkb.lock.lockRecord.id() == id).collect(Collectors.toList());
			if(match.isEmpty())
				continue;
			world.playSound(ctx.getPlayer(), pos, LocksSoundEvents.LOCK_OPEN, SoundSource.BLOCKS, 1f, 1f);
			if(world.isClientSide)
				return InteractionResult.SUCCESS;
			for(Lockable lkb : match)
				lkb.lock.setLocked(!lkb.lock.isLocked());
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}
}