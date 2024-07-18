/*
 * This file is apart of Beaver Mod <https://github.com/Beaver-Mod/Beaver-Mod>
 * Copyright (C) 2024  Beaver Fan Club
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.beaverfanclub.beavermod.util.minecraft;

import me.beaverfanclub.beavermod.Beaver;
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
