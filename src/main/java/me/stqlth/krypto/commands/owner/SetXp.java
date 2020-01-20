package me.stqlth.krypto.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.XpMessages;
import me.stqlth.krypto.xpSystem.XpMethods;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SetXp extends Command {

    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    private XpMethods xpMethods;
    private XpMessages xpMessages;

    public SetXp(KryptoConfig kryptoConfig, DebugMessages debugMessages, XpMethods xpMethods, XpMessages xpMessages) {
        this.name = "setxp";
        this.arguments = "<@user> <xp>";
        this.help = "Set the xp of a user.";
        this.guildOnly = true;
        this.ownerCommand = true;
        this.hidden = true;

        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
        this.xpMethods = xpMethods;
        this.xpMessages = xpMessages;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!event.getMember().getId().equals(kryptoConfig.getOwnerId())) {
            event.getMessage().delete().queue();
            xpMessages.onlyOwner(event.getTextChannel()); //Only Stqlth can use this command, souly for testing, no server owner should avoid the xp system
            return;
        } //Makes sure no owner can use this

        String[] args = event.getMessage().getContentRaw().split(" ");
        int xp;

        if (args.length < 3) {
            event.getMessage().delete().queue();
            xpMessages.sendSetXpErrorMessage(event.getTextChannel(), event.getMember(), event, getName());
            return;
        }

        Member target = event.getMessage().getMentionedMembers().get(0);
        int userId = xpMethods.getUserId(target);

        try {
            xp = Integer.parseInt(args[2]); //try to parse arg 3 if not NumberFormatException
        } catch (NumberFormatException ex) {
            event.getMessage().delete().queue();
            xpMessages.sendSetXpErrorMessage(event.getTextChannel(), event.getMember(), event, getName());
            return;
        }

        setPlayerXp(xp, event, userId, target);
    }

    private void setPlayerXp(int xp, CommandEvent event, int userId, Member target) {

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            statement.execute("UPDATE xp SET XpAmount='" + xp + "' WHERE GuildId='" + xpMethods.getGuildId(event) + "' AND UserId='" + userId + "'");
            xpMessages.setPlayerXp(event.getTextChannel(), target, xp);
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }
}
