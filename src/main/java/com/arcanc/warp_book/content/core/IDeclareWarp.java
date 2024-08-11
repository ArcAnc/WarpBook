package com.arcanc.warp_book.content.core;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IDeclareWarp
{
  /** Used by the warpbook for generating it's listing.  Must be used for warp book pages */
  Component getName(Item.TooltipContext ctx, ItemStack stack);
  
  /** Gets the waypoint for this object */
  GlobalPos getWaypoint(Player player, ItemStack stack);
  
  /** Does this stack have valid waypoint data? */
  boolean hasValidData(ItemStack stack);
  
}
