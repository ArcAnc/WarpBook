/**
 * @author ArcAnc
 * Created at: 07.08.2024
 * Copyright (c) 2024
 * <p>
 * This code is licensed under "Arc's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.warp_book.content.network.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IPacket extends CustomPacketPayload
{
    void handle(IPayloadContext ctx);
}
