package com.arcanc.warp_book;

import com.arcanc.warp_book.content.core.WarpDrive;
import com.arcanc.warp_book.registration.Registration;
import com.arcanc.warp_book.util.EventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Database.MOD_ID)
public class WarpBook
{
	public static final Logger logger = LogManager.getLogger(Database.MOD_ID);

	public static WarpDrive warpDrive = new WarpDrive();

	public static double exhaustionCoefficient =  0.d;
	public static double minExhaustionDistance = 256.d;
	public static double maxExhaustionDistance = 16384.d;
	public static double distanceCoefficient = 1/256d;
	public static boolean deathPagesEnabled = true;


	public WarpBook(final IEventBus modEventBus, final ModContainer modContainer)
	{
		Registration.init(modEventBus);

		EventHandler.initEvents(modEventBus);
	}
}
