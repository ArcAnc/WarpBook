package com.arcanc.warp_book.content.item;

import com.arcanc.warp_book.Database;
import com.arcanc.warp_book.content.core.WarpColors;
import com.arcanc.warp_book.content.gui.GuiBook;
import com.arcanc.warp_book.content.gui.inventory.MenuWarpBook;
import com.arcanc.warp_book.content.gui.inventory.container.ContainerWarpBook;
import com.arcanc.warp_book.content.gui.inventory.container.ContainerWarpBookSpecial;
import com.arcanc.warp_book.registration.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class WarpBookItem extends Item implements IColorable, Registration.ItemRegistry.IMustBeAddedToCreative
{
	
	public WarpBookItem()
	{
		super(new Item.Properties().stacksTo(1).
				component(DataComponents.CONTAINER, new ItemContainerContents(54)).
				component(Registration.DataComponentRegistry.WARP_BOOK_DEATHLY, 0));
	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity)
	{
		return 1;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(
			@NotNull Level level,
			@NotNull Player player,
			@NotNull InteractionHand usedHand)
	{
		ItemStack itemStack = player.getItemInHand(usedHand);

		if (player.isCrouching())
		{
			if (player instanceof ServerPlayer serverPlayer)
				serverPlayer.openMenu(new MenuProvider()
				{
					@Override
					public @NotNull Component getDisplayName() {
						return Component.empty();
					}

					@Override
					public @NotNull MenuWarpBook createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player)
					{
						return new MenuWarpBook(containerId, playerInventory, new ContainerWarpBook(itemStack), new ContainerWarpBookSpecial(itemStack));
					}
				}, byteBuf -> ItemStack.OPTIONAL_STREAM_CODEC.encode(byteBuf, itemStack));

		}
		else
		{
			if (level.isClientSide())
				Minecraft.getInstance().setScreen(new GuiBook(player, usedHand));
		}

		return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
	}

	@Override
	public void appendHoverText(
			@NotNull ItemStack stack,
			@NotNull TooltipContext context,
			@NotNull List<Component> tooltipComponents,
			@NotNull TooltipFlag tooltipFlag)
	{
		int amount = countNonEmptyAmount(WarpBookItem.getContent(stack).nonEmptyStream().collect(Collectors.toList()));

		tooltipComponents.add(Component.translatable(Database.GUI_TEXT_WARP_BOOK_TOOLTIP, amount));
	}

	@Override
	public boolean isRepairable(ItemStack stack)
	{
		return false;
	}

	public static @NotNull ItemContainerContents getContent(@NotNull ItemStack stack)
	{
		return stack.getOrDefault(DataComponents.CONTAINER, new ItemContainerContents(54));
	}

	public static void setWarpBookContent(@NotNull ItemStack stack, @NotNull ItemContainerContents content)
	{
		stack.set(DataComponents.CONTAINER, content);
	}

	public static int getRespawnsLeft(@NotNull ItemStack item)
	{
		return item.getOrDefault(Registration.DataComponentRegistry.WARP_BOOK_DEATHLY, 0);
	}
	
	public static void setRespawnsLeft(@NotNull ItemStack item, int deaths)
	{
		if (item.has(Registration.DataComponentRegistry.WARP_BOOK_DEATHLY))
			item.set(Registration.DataComponentRegistry.WARP_BOOK_DEATHLY, deaths);
	}
	
	public static void decrRespawnsLeft(ItemStack item)
	{
		setRespawnsLeft(item, getRespawnsLeft(item) - 1);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack, int tintIndex)
	{
        return switch (tintIndex)
		{
            case 0 -> WarpColors.LEATHER.getColor();
            case 1 -> WarpColors.UNBOUND.getColor();
            default -> 0xFFFFFFFF;
        };
	}

	@Override
	public boolean addToCreative()
	{
		return true;
	}

	@Override
	public @NotNull String getDescriptionId()
	{
		return WarpItem.getDescription(this);
	}

	public static boolean inventoryIsEmpty(@NotNull List<ItemStack> list)
	{
		for (ItemStack stack : list)
			if (!stack.isEmpty())
				return false;
		return true;
	}

	public static int countNonEmptyAmount(@NotNull List<ItemStack> list)
	{
		int q = 0;

		for (ItemStack stack : list)
			if (!stack.isEmpty())
				q++;
		return q;
	}
}
