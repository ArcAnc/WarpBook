package com.arcanc.warp_book.content.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IColorable
{
	@OnlyIn(Dist.CLIENT)
	int getColor(ItemStack stack, int tintIndex);
}
