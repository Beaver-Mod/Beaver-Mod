package me.drred96.beaver;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = Beaver.MOD_ID, name = Beaver.NAME, version = Beaver.VERSION, acceptedMinecraftVersions = Beaver.ACCEPTED_VERSIONS, clientSideOnly = true)
public class Beaver {

    public static final Logger logger = LogManager.getLogger("Beaver");

    public static final String MOD_ID = "keystrokesmod";
    public static final String NAME = "Beaver";
    public static final String VERSION = "1.2";
    public static final String ACCEPTED_VERSIONS = "[1.8.9]";

    public Minecraft mc;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Loading Beaver Mod");
        this.mc = Minecraft.getMinecraft();
    }



}
