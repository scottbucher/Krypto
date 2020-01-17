package me.stqlth.krypto.commands.xp.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.StaffMessages;
import me.stqlth.krypto.messages.discordOut.XpMessages;
import me.stqlth.krypto.xpSystem.XpMethods;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.*;

public class RemoveLevelingReward extends Command {

    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    private XpMethods xpMethods;
    private XpMessages xpMessages;
    private StaffMessages staffMessages;

    public RemoveLevelingReward(KryptoConfig kryptoConfig, DebugMessages debugMessages, XpMethods xpMethods, XpMessages xpMessages, StaffMessages staffMessages) {
        this.name = "removelevelingreward";
        this.help = "Remove all role rewards for reaching a level.";
        this.guildOnly = true;
        this.hidden = true;

        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
        this.xpMethods = xpMethods;
        this.xpMessages = xpMessages;
        this.staffMessages = staffMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        Permission req = Permission.ADMINISTRATOR;
        Member member = event.getMember();
        TextChannel channel = event.getTextChannel();
        Guild g = event.getGuild();

        if (!member.hasPermission(req)) { //Only admins may use this command
            staffMessages.onlyAdmins(channel);
            return;
        }

        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) { //need atleast 2 args for this command default command & level
            staffMessages.sendRemoveLevelingRewardsFormatError(channel, member, event, getName());
            return;
        }

        int level;

        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            staffMessages.sendRemoveLevelingRewardsFormatError(channel, member, event, getName());
            return;
        }

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            int guildId = xpMethods.getGuildId(event);

            ResultSet check = statement.executeQuery("SELECT COUNT(*) > 0 AS AlreadyExists FROM guild WHERE DiscordId='" + g.getId() + "'");
            check.next();

            statement.execute("DELETE FROM rewards WHERE GuildId='" + guildId + "' AND Level='" + level + "'");
            xpMessages.removeLevelingReward(channel, level);

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
            return;
        }

    }
}
