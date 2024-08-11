package com.arcanc.warp_book.content.core;

public enum WarpColors
{
	
	UNBOUND(0xFFC3C36A),//The default tawny color of a warp page
	BOUND(0xFF098C76),//The color of an ender pearl
	PLAYER(0xFF8e0000),//Red color.. blood maybe?
	DEATHLY(0xFF131313),//Near Black
	LEATHER(0xFF654b17);//The color of vanilla books
	
	private final int	color;
	
	WarpColors(int color)
	{
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
}
