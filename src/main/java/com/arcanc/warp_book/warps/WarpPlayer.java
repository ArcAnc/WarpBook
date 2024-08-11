package com.arcanc.warp_book.warps;

import com.arcanc.warp_book.content.core.WarpColors;
import com.arcanc.warp_book.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class WarpPlayer extends Warp
{
	
	@Override
	public Component getName(@NotNull Item.TooltipContext ctx, @NotNull ItemStack stack)
	{
		if (hasValidData(stack))
		{
			return Component.literal(stack.get(Registration.DataComponentRegistry.NAME_IN_BOOK));
		}
		return Component.literal(unbound);
	}
	
	@Override
	public GlobalPos getWaypoint(@NotNull Player player, ItemStack stack)
	{
		if(hasValidData(stack))
		{
			UUID playerID = stack.get(Registration.DataComponentRegistry.TARGET_UUID);

			Player targetPlayer = player.level().getPlayerByUUID(playerID);

			if (targetPlayer != null)
			{
				return new GlobalPos(targetPlayer.level().dimension(), new BlockPos((int)targetPlayer.getX(), (int)targetPlayer.getEyeY(), (int)targetPlayer.getZ()));
			}
		}

		return null;
	}
	
	@Override
	public boolean hasValidData(@NotNull ItemStack stack)
	{
		return stack.has(Registration.DataComponentRegistry.TARGET_UUID);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag flagIn)
	{
		tooltip.add(Component.literal(ttprefix).append(getName(context, stack)));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public WarpColors getColor() {
		return WarpColors.PLAYER;
	}
	
}
