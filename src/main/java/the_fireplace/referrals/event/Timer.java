package the_fireplace.referrals.event;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import the_fireplace.referrals.Referrals;
import the_fireplace.referrals.logic.TimerLogic;

@Mod.EventBusSubscriber(modid = Referrals.MODID)
public class Timer {
    private static int fiveMinuteCounter = 0;
    private static boolean executing = false;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(!executing) {
            if(++fiveMinuteCounter >= 20*60*5) {
                executing = true;
                fiveMinuteCounter -= 20*60*5;
                TimerLogic.runFiveMinuteLogic();
                executing = false;
            }
        }
    }
}
