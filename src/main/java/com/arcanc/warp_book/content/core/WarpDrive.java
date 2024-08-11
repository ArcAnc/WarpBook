package com.arcanc.warp_book.content.core;

import com.arcanc.warp_book.Database;
import com.arcanc.warp_book.WarpBook;
import com.arcanc.warp_book.content.network.NetworkEngine;
import com.arcanc.warp_book.content.network.packet.S2CPacketEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WarpDrive
{
	
	public void processWarp(@NotNull Player player, @NotNull GlobalPos globalPos)
	{
		Level level = player.level();
		S2CPacketEffect oldDim = new S2CPacketEffect(false, (int)player.getX(), (int)player.getY(), (int)player.getZ());
		S2CPacketEffect newDim = new S2CPacketEffect(true, globalPos.pos().getX(), globalPos.pos().getY(), globalPos.pos().getZ());
		Vec3 oldPoint = player.position();
		
		boolean crossDim = level.dimension().equals(globalPos.dimension());
		
		if (!level.isClientSide())
		{
			ServerLevel serverLevel = level.getServer().getLevel(globalPos.dimension());
			teleportToPos(player, serverLevel, globalPos.pos().getCenter(), null);
		}

		double dx = globalPos.pos().getCenter().x - oldPoint.x;
		double dy = globalPos.pos().getCenter().y - oldPoint.y;
		double dz = globalPos.pos().getCenter().z - oldPoint.z;
		double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
		
		if(crossDim)
			distance = Double.POSITIVE_INFINITY;

		
		//Update player
		player.causeFoodExhaustion((float)calculateExhaustion(player.level().getDifficulty(), distance));

		//Send effect packets
		if (!level.isClientSide())
		{
			ServerLevel serverLevel = level.getServer().getLevel(globalPos.dimension());
			NetworkEngine.sendToPlayerNear((ServerLevel) level, null, oldPoint, 64, oldDim);
			NetworkEngine.sendToPlayerNear(serverLevel, null, globalPos.pos().getCenter(), 64, newDim);
		}
	}

	private static void teleportToPos(
			Player target,
			ServerLevel level,
			@NotNull Vec3 position,
			@Nullable Vec2 rotation
	)
	{
		if (rotation == null)
		{
			performTeleport(target, level, position.x, position.y, position.z, target.getYRot(), target.getXRot());
		}
		else
		{
			performTeleport(target, level, position.x, position.y, position.z, rotation.y, rotation.x);
		}
	}

	private static void performTeleport(
			Player player,
			ServerLevel level,
			double x,
			double y,
			double z,
			float yaw,
			float pitch
	)
	{
		net.neoforged.neoforge.event.entity.EntityTeleportEvent.TeleportCommand event = net.neoforged.neoforge.event.EventHooks.onEntityTeleportCommand(player, x, y, z);
		if (event.isCanceled())
		{
			return;
		}
		x = event.getTargetX();
		y = event.getTargetY();
		z = event.getTargetZ();

		BlockPos blockpos = BlockPos.containing(x, y, z);
		if (!Level.isInSpawnableBounds(blockpos))
			player.sendSystemMessage(Component.translatable(Database.MESSAGE_ERROR_INVALID_POSITION));
		else {
			float f = Mth.wrapDegrees(yaw);
			float f1 = Mth.wrapDegrees(pitch);
			if (player.teleportTo(level, x, y, z, EnumSet.noneOf(RelativeMovement.class), f, f1))
			{

				player.setDeltaMovement(player.getDeltaMovement().multiply(1.0, 0.0, 1.0));
				player.setOnGround(true);
			}
		}
	}

	private static double calculateExhaustion(@NotNull Difficulty difficultySetting, double distance)
	{
		
		distance = Mth.clamp(distance, WarpBook.minExhaustionDistance, WarpBook.maxExhaustionDistance);
		double distanceFactor = distance * WarpBook.distanceCoefficient;
		
		float scaleFactor = switch (difficultySetting)
		{
            case EASY -> 1.0f;
            case NORMAL -> 1.5f;
            case HARD -> 2.0f;
            case PEACEFUL -> 0.0f;
        };

        return WarpBook.exhaustionCoefficient * scaleFactor * distanceFactor;
	}
	
}
