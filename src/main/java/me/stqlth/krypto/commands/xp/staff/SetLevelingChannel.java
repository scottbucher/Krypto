package me.stqlth.krypto.commands.xp.staff;

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

public class SetLevelingChannel extends Command {
    private KryptoConfig kryptoConfig;
    private StaffMessages staffMessages;
    private DebugMessages debugMessages;

    public SetLevelingChannel(KryptoConfig kryptoConfig, StaffMessages staffMessages, DebugMessages debugMessages) {
        this.name = "setlevelchannel";
        this.help = "Set the channel for leveling messages.";
        this.guildOnly = true;
        this.hidden = true;
        this.kryptoConfig = kryptoConfig;
        this.staffMessages = staffMessages;
        this.debugMessages = debugMessages;
    }

    @Override
    protected void execute(CommandEvent event) {
        TextChannel channel = event.getTextChannel();

        Guild g = event.getGuild();
        Member sender = event.getMember();
        Permission req = Permission.ADMINISTRATOR;

        if (!sender.hasPermission(req)) {
            staffMessages.onlyAdmins(channel); //Only admins may use this command
            return;
        }

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            int gSettingsId=0;
            ResultSet id = statement.executeQuery("CALL GetGuildSettingsId(" + event.getGuild().getId() + ")");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            statement.execute("CALL UpdateLevelingChannel(" + event.getTextChannel().getId() + ", " + gSettingsId + ")");
            staffMessages.setLevelingChannel(channel);
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

    }
}
