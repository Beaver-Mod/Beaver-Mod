package me.beavermod.module.impl.combat;

import me.beavermod.event.ReceivePacketEvent;
import me.beavermod.module.Module;
import me.beavermod.module.setting.impl.FloatSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

    private final FloatSetting amount = new FloatSetting("Amount", "Amount of velocity you take", 0.0F, 100.0F, 90.0F);

    public Velocity() {
        super("Velocity", "Reduces your knockback", Category.COMBAT);
        addSettings(amount);
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {

        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                event.setCanceled(true);

                mc.thePlayer.motionY = (double)packet.getMotionY() / 8000.0;

                if (amount.get() != 0.0F) {
                    mc.thePlayer.motionX = ((double)packet.getMotionX() / 8000.0) * (double)(amount.get() / 100.0F);
                    mc.thePlayer.motionZ = ((double)packet.getMotionZ() / 8000.0) * (double)(amount.get() / 100.0F);
                }

            }
        }

    }

}
