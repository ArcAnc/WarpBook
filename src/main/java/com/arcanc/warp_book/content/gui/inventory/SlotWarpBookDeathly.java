package com.arcanc.warp_book.content.gui.inventory;

import com.arcanc.warp_book.content.item.DeathlyWarpPageItem;
import com.arcanc.warp_book.content.gui.inventory.container.ContainerWarpBookSpecial;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotWarpBookDeathly extends Slot
{
	public SlotWarpBookDeathly(ContainerWarpBookSpecial inventorySpecial, int i, int j, int k)
	{
		super(inventorySpecial, i, j, k);
	}
	
	public static boolean itemValid(ItemStack itemStack) {
		return itemStack.getItem() instanceof DeathlyWarpPageItem;
	}
	
	@Override
	public boolean mayPlace(@NotNull ItemStack itemStack) {
		return itemValid(itemStack);
	}
	
}
