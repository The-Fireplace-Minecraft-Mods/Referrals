package the_fireplace.referrals.event;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import the_fireplace.referrals.Referrals;
import the_fireplace.referrals.logic.PlayerEventLogic;

@Mod.EventBusSubscriber(modid = Referrals.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEventLogic.onLogin(event.player);
    }
}
