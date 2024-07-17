package me.beavermod.module.impl.macro;

import me.beavermod.event.ReceivePacketEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.BooleanSetting;
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

    private final IntSetting tickDelay = new IntSetting("Tick Delay", "Delay in ticks between playing a note (may need to be adjusted between songs)", 0, 10, 2);
    private final BooleanSetting prevHit = new BooleanSetting("Early Note", "Hits the note early, may help on higher ping", false);

    private boolean differentState = true;
    private int ticks = 0;

    public AutoHarp() {
        super("Auto Harp", "Plays melodies harp for you", Category.MACRO);
        addSettings(tickDelay, prevHit);
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
                    if (prevHit.get()) {
                        for (int slot = 28; slot <= 34; ++slot) {
                            ItemStack itemStack = inventory.getStackInSlot(slot);
                            if (itemStack != null && Item.getIdFromItem(itemStack.getItem()) == 35) {
                                playNote(itemStack, slot);
                                break;
                            }
                        }
                    } else {
                        for (int slot = 37; slot <= 43; ++slot) {
                            ItemStack itemStack = inventory.getStackInSlot(slot);
                            if (itemStack != null && Item.getIdFromItem(itemStack.getItem()) == 155) {
                                playNote(itemStack, slot);
                                break;
                            }
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
