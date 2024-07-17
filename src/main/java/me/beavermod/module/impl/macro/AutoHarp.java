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

package me.beavermod.module.impl.macro;

import me.beavermod.event.ReceivePacketEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.IntSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoHarp extends Module {

    private final IntSetting tickDelay = new IntSetting("Tick Delay", "Delay in ticks between playing a note (may need to be adjusted between songs)", 0, 5, 2);

    private boolean differentState = true;
    private int ticks = 0;

    public AutoHarp() {
        super("Auto Harp", "Plays melodies harp for you (won't work as well on high ping)", Category.MACRO);
        addSettings(tickDelay);
    }

    @Override
    public void onEnabled() {
        differentState = true;
        ticks = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {

        ticks--;

        try {
            if (event.phase == TickEvent.Phase.START && mc.currentScreen instanceof GuiChest && isUsingHarp()) {
                IInventory inventory = ((ContainerChest) mc.thePlayer.openContainer).getLowerChestInventory();

                if (differentState && ticks <= 0) {
                    for (int slot = 37; slot <= 43; ++slot) {
                        ItemStack itemStack = inventory.getStackInSlot(slot);
                        if (itemStack != null && Item.getIdFromItem(itemStack.getItem()) == 155) {
                            playNote(itemStack, slot);
                            break;
                        }
                    }
                }
            }
        } catch (NullPointerException | ClassCastException ignored) {}
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof S2FPacketSetSlot) {
            if (!isUsingHarp()) {
                return;
            }

            S2FPacketSetSlot packet = (S2FPacketSetSlot)event.getPacket();
            if (packet.func_149174_e() != null) {
                differentState = true;
            }
        }
    }

    private void playNote(ItemStack itemStack, int slot) {
        mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(
                mc.thePlayer.openContainer.windowId,
                slot,
                0,
                3,
                itemStack,
                mc.thePlayer.openContainer.getNextTransactionID(mc.thePlayer.inventory)
        ));

        differentState = false;
        ticks = tickDelay.get();
    }

    private boolean isUsingHarp() {
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            return ((ContainerChest)mc.thePlayer.openContainer).getLowerChestInventory().getName().startsWith("Harp - ");
        }
        return false;
    }


}
