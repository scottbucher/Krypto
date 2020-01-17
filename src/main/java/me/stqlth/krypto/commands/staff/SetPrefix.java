package me.stqlth.krypto.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.StaffMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.*;

public class SetPrefix extends Command {
    private KryptoConfig kryptoConfig;
    private StaffMessages staffMessages;
    private DebugMessages debugMessages;

    public SetPrefix(KryptoConfig kryptoConfig, StaffMessages staffMessages, DebugMessages debugMessages) {
        this.name = "setprefix";
        this.help = "Set the bot prefix.";
        this.guildOnly = true;
        this.hidden = true;
        this.kryptoConfig = kryptoConfig;
        this.staffMessages = staffMessages;
        this.debugMessages = debugMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();

        String[] args = event.getMessage().getContentRaw().split(" ");
        Guild g = event.getGuild();
        Member sender = event.getMember();
        Permission req = Permission.ADMINISTRATOR;

        if (!sender.hasPermission(req)) {
            staffMessages.onlyAdmins(channel);
            return;
        }
        if (args.length != 2) {
            staffMessages.sendErrorMessagePrefix(channel, sender, event, getName());
            return;
        }

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            int gSettingsId=0;
            ResultSet id = statement.executeQuery("SELECT GuildSettingsId FROM guild WHERE DiscordId='" + g.getId() + "'");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            statement.execute("UPDATE guildsettings SET Prefix='" + args[1] + "' WHERE GuildSettingsId='" + gSettingsId + "'");
            staffMessages.setPrefix(channel, args[1]);
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }


    }
}
