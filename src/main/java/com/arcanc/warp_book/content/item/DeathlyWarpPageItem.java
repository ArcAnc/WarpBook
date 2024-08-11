package com.arcanc.warp_book.content.item;

import com.arcanc.warp_book.content.core.WarpColors;
import com.arcanc.warp_book.registration.Registration;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DeathlyWarpPageItem extends Item implements IColorable, Registration.ItemRegistry.IMustBeAddedToCreative
{
	public DeathlyWarpPageItem(@NotNull Item.Properties props)
	{
		super(props.stacksTo(16));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack, int tintIndex)
	{
        return switch (tintIndex)
		{
            case 0 -> pageColor();
            case 1 -> symbolColor();
            default -> 0xFFFFFFFF;
        };
	}
	
	@OnlyIn(Dist.CLIENT)
	public int pageColor() {
		return WarpColors.DEATHLY.getColor();
	}
	
	@OnlyIn(Dist.CLIENT)
	public int symbolColor() {
		return 0xFFBBBBBB;
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
}
