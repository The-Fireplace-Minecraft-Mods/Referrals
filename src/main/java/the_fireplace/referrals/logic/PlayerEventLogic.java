package the_fireplace.referrals.logic;

import net.minecraft.entity.player.EntityPlayer;
import the_fireplace.referrals.data.PlayerData;

public class PlayerEventLogic {
    public static void onLogin(EntityPlayer player) {
        if(!PlayerData.hasFirstLoginTimestamp(player.getUniqueID()))
            PlayerData.setFirstLoginTimestamp(player.getUniqueID());
    }
}
