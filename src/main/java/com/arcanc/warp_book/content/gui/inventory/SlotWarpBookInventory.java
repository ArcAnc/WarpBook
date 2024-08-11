package com.arcanc.warp_book.content.gui.inventory;

import com.arcanc.warp_book.content.item.WarpBookItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class SlotWarpBookInventory extends Slot
{
	public SlotWarpBookInventory(Inventory inventory, int i, int j, int k) {
		super(inventory, i, j, k);
	}
	
	@Override
	public boolean mayPickup(@NotNull Player player)
	{
		return hasItem() && !(this.getItem().getItem() instanceof WarpBookItem);
	}
	
}
