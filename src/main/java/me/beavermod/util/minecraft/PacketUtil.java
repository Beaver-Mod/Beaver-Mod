/*
 * This file is part of Beaver Mod.
 * Copyright (c) Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>.
 *
 * Beaver Mod is free software: permission is granted to use, modify or
 * distribute this file under the terms of the MIT license.
 */

package me.beavermod.util.minecraft;

import me.beavermod.Beaver;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.List;

public class PacketUtil {

    public static List<Packet> noEventList = new ArrayList<>();

    public static void send(Packet packet) {
        Beaver.INSTANCE.mc.getNetHandler().addToSendQueue(packet);
    }

    public static void sendNoEvent(Packet packet) {
        noEventList.add(packet);
        Beaver.INSTANCE.mc.getNetHandler().addToSendQueue(packet);
    }

}
