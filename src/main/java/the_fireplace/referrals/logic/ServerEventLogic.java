package the_fireplace.referrals.logic;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import the_fireplace.referrals.Config;
import the_fireplace.referrals.commands.CommandReferredBy;
import the_fireplace.referrals.data.PlayerData;

public class ServerEventLogic {
    public static void onServerStarting(MinecraftServer server) {
        //Init config
        Config.getInstance();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new CommandReferredBy());
    }
    public static void onServerStopping() {
        PlayerData.save();
    }
}
