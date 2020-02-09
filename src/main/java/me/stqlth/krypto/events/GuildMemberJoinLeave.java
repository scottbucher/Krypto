package me.stqlth.krypto.events;

import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.JoinLeaveMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;

public class GuildMemberJoinLeave extends ListenerAdapter {
    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    private JoinLeaveMessages joinLeaveMessages;

    public GuildMemberJoinLeave(KryptoConfig kryptoConfig, DebugMessages debugMessages, JoinLeaveMessages joinLeaveMessages) {
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
        this.joinLeaveMessages = joinLeaveMessages;
    }

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (!event.getMember().getUser().isBot()) {
            joinLeaveMessages.welcomeMessage(event);
        }
        addUser(event);

        Role guest = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("guest")).findFirst().orElse(null);

        if (guest != null)
            event.getGuild().addRoleToMember(event.getMember(), guest).queue();

    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (!event.getMember().getUser().isBot()) {
            joinLeaveMessages.leaveMessage(event);
        }

    }


    private void addUser(GuildMemberJoinEvent event) {

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            Guild g = event.getGuild();

            ResultSet guildId = statement.executeQuery("CALL GetGuildId(" + g.getId() + ")");
            guildId.next();
            int discordId = guildId.getInt("GuildId");

            ResultSet check = statement.executeQuery("CALL DoesUserAlreadyExist(" + event.getMember().getId() + ")");
            check.next();
            boolean UserAlreadyExists = check.getBoolean("UserAlreadyExists");


            if (!UserAlreadyExists) {
                statement.execute("CALL InsertUser(" + event.getMember().getId() + ")");
            }


            ResultSet UserId = statement.executeQuery("CALL GetUserId(" + event.getMember().getId() + ")");
            UserId.next();
            int userId = UserId.getInt("UserId");

            ResultSet check2 = statement.executeQuery("CALL DoesDiscordAndUserExistInXp(" + discordId + ", " + userId + ")");
            check2.next();
            boolean DiscordAndUserExistsInXp = check2.getBoolean("DiscordAndUserExistsInXp");


            if (!DiscordAndUserExistsInXp) {
                statement.execute("CALL InsertXp(" + discordId + ", " + userId + ")");
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }
}
