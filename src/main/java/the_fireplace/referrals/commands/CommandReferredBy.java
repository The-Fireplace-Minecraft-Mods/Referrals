package the_fireplace.referrals.commands;

import com.mojang.authlib.GameProfile;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import the_fireplace.referrals.Config;
import the_fireplace.referrals.data.PlayerData;
import the_fireplace.referrals.util.TextStyles;
import the_fireplace.referrals.util.translation.TranslationUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CommandReferredBy extends CommandBase {

    @Override
    public String getName() {
        return "referredby";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return TranslationUtil.getRawTranslationString(sender, "commands.referredby.usage");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length <= 0)
            throw new WrongUsageException(getUsage(sender));
        EntityPlayerMP referredPlayer = (EntityPlayerMP) sender;
        if(PlayerData.isReferred(referredPlayer.getUniqueID())) {
            sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.referredby.already_referred").setStyle(TextStyles.RED));
            return;
        }
        String name = args[0].toLowerCase();
        GameProfile referrer = server.getPlayerProfileCache().getGameProfileForUsername(name);
        if(referrer != null) {
            if(!PlayerData.hasFirstLoginTimestamp(referrer.getId())) {
                sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.referredby.never_joined", name).setStyle(TextStyles.RED));
                return;
            }
            if(PlayerData.getFirstLoginTimestamp(referredPlayer.getUniqueID()) < PlayerData.getFirstLoginTimestamp(referrer.getId())) {
                sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.referredby.wrong_timestamp", name).setStyle(TextStyles.RED));
                return;
            }
            int referralnum = PlayerData.getReferralsMade(referrer.getId())+1;
            List<String> commandList;
            if(Config.getInstance().rankedRewards.containsKey(referralnum))
                commandList = Config.getInstance().rankedRewards.get(referralnum);
            else
                commandList = Config.getInstance().standardRewards;
            for(String command: commandList) {
                //noinspection RegExpRedundantEscape
                command = command.replaceAll("\\[referrer\\]", referrer.getName())
                    .replaceAll("\\[referred\\]", referredPlayer.getName());
                server.getCommandManager().executeCommand(server, command);
            }
            PlayerData.setReferred(referredPlayer.getUniqueID());
            PlayerData.incrementReferralsMade(referrer.getId());
        } else
            throw new PlayerNotFoundException("commands.generic.player.notFound", name);
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }
}
