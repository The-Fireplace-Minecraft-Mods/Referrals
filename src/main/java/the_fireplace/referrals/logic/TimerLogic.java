package the_fireplace.referrals.logic;

import the_fireplace.referrals.data.PlayerData;

public class TimerLogic {
    public static void runFiveMinuteLogic() {
        PlayerData.save();
    }
}
