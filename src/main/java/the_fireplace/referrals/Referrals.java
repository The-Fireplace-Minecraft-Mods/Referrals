package the_fireplace.referrals;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.referrals.logic.ServerEventLogic;

import java.util.Objects;

import static the_fireplace.referrals.Referrals.*;

@Mod.EventBusSubscriber(modid = MODID)
@Mod(modid = MODID, name = MODNAME, version = VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*", dependencies="after:grandeconomy;after:dynmap;after:spongeapi;required-after:forge@[14.23.5.2817,)", certificateFingerprint = "${fingerprint}")
public final class Referrals {
    public static final String MODID = "referrals";
    public static final String MODNAME = "Referrals";
    public static final String VERSION = "${version}";

    private static Logger LOGGER = FMLLog.log;

    private boolean validJar = true;

    public static Logger getLogger() {
        return LOGGER;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        if(!validJar)
            LOGGER.error("The jar's signature is invalid! Please redownload from " + Objects.requireNonNull(Loader.instance().activeModContainer()).getUpdateUrl());
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ServerEventLogic.onServerStarting(event.getServer());
    }

    @Mod.EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
        ServerEventLogic.onServerStopping();
    }

    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent e) {
        if(!e.isDirectory()) {
            validJar = false;
        }
    }
}
