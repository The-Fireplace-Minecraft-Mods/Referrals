package the_fireplace.referrals.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerData {
    private static final HashMap<UUID, PlayerStoredData> playerData = Maps.newHashMap();
    public static final File playerDataLocation = new File(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getSaveHandler().getWorldDirectory(), "referrals");

    public static int getReferralsMade(UUID player) {
        return getPlayerData(player).referralsMade;
    }

    public static void incrementReferralsMade(UUID player) {
        getPlayerData(player).incrementReferralsMade();
    }

    public static long getFirstLoginTimestamp(UUID player) {
        return getPlayerData(player).firstLogin;
    }

    public static boolean hasFirstLoginTimestamp(UUID player) {
        return getPlayerData(player).firstLogin != 0;
    }

    public static void setFirstLoginTimestamp(UUID player) {
        getPlayerData(player).setFirstLoginTimestamp();
    }

    public static boolean isReferred(UUID player) {
        return getPlayerData(player).referred;
    }

    public static void setReferred(UUID player) {
        getPlayerData(player).setReferred();
    }

    public static void setShouldDisposeReferences(UUID player, boolean shouldDisposeReferences) {
        getPlayerData(player).shouldDisposeReferences = shouldDisposeReferences;
    }

    private static PlayerStoredData getPlayerData(UUID player) {
        if(!playerData.containsKey(player))
            playerData.put(player, new PlayerStoredData(player));
        return playerData.get(player);
    }

    public static void save() {
        for(Map.Entry<UUID, PlayerStoredData> entry : Sets.newHashSet(playerData.entrySet())) {
            entry.getValue().save();
            if(entry.getValue().shouldDisposeReferences)
                playerData.remove(entry.getKey());
        }
    }

    private static class PlayerStoredData {
        private final File playerDataFile;
        private boolean isChanged, saving, shouldDisposeReferences = false;

        private long firstLogin;
        private int referralsMade;
        private boolean referred;

        private PlayerStoredData(UUID playerId) {
            playerDataFile = new File(playerDataLocation, playerId.toString()+".json");
            if(!load()) {
                referralsMade = 0;
                referred = false;
                firstLogin = 0;
            }
            //If the player is offline, we should remove references so garbage collection can clean it up when the data is done being used.
            //noinspection ConstantConditions
            if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId) == null)
                shouldDisposeReferences = true;
        }

        /**
         * @return true if it loaded from a file successfully, false otherwise.
         */
        private boolean load() {
            if(!playerDataLocation.exists()) {
                playerDataLocation.mkdirs();
                return false;
            }

            JsonParser jsonParser = new JsonParser();
            try {
                Object obj = jsonParser.parse(new FileReader(playerDataFile));
                if(obj instanceof JsonObject) {
                    JsonObject jsonObject = (JsonObject) obj;
                    referralsMade = jsonObject.has("referralsMade") ? jsonObject.getAsJsonPrimitive("referralsMade").getAsInt() : 0;
                    firstLogin = jsonObject.has("firstLogin") ? jsonObject.getAsJsonPrimitive("firstLogin").getAsLong() : 0;
                    referred = jsonObject.has("referred") && jsonObject.getAsJsonPrimitive("referred").getAsBoolean();
                    return true;
                }
            } catch (FileNotFoundException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private void save() {
            if(!isChanged || saving)
                return;
            saving = true;
            new Thread(() -> {
                JsonObject obj = new JsonObject();
                obj.addProperty("referralsMade", referralsMade);
                obj.addProperty("firstLogin", firstLogin);
                obj.addProperty("referred", referred);

                try {
                    FileWriter file = new FileWriter(playerDataFile);
                    file.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saving = isChanged = false;
            }).start();
        }

        public void incrementReferralsMade() {
            this.referralsMade++;
            isChanged = true;
        }

        public void setReferred() {
            this.referred = true;
            isChanged = true;
        }

        public void setFirstLoginTimestamp() {
            this.firstLogin = System.currentTimeMillis();
            isChanged = true;
        }
    }
}
