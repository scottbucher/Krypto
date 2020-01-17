package me.stqlth.krypto.events;

import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.*;


public class GuildJoinLeave extends ListenerAdapter {
    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;

    public GuildJoinLeave(KryptoConfig kryptoConfig, DebugMessages debugMessages) {
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
    }

    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        Logger.Info("Guild \"" + guild.getName() + "\" (" + guild.getId() + ") joined!");

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet check = statement.executeQuery("SELECT COUNT(*) > 0 AS AlreadyExists FROM guild WHERE DiscordId='" + guild.getId() + "'");
            check.next();
            boolean alreadyExists = check.getBoolean("AlreadyExists");

            if (!alreadyExists) {
                statement.execute("INSERT INTO guildsettings (Prefix) VALUES('!')");
                ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
                rs.next();
                int lastId = rs.getInt("LAST_INSERT_ID()");
                statement.execute("INSERT INTO guild (DiscordId, GuildSettingsId, Active) " + "VALUES('" + guild.getId() + "', '" + lastId + "', '1')");
            } else {
                statement.execute("UPDATE guild SET Active=true WHERE DiscordId='" + guild.getId() + "'");
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        for (Member mem : event.getGuild().getMembers()) {
            addUser(event, mem);
        }

    }

    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        Logger.Info("Guild \"" + guild.getName() + "\" (" + guild.getId() + ") left!");

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            statement.execute("CALL UpdateGuildActive(" + guild.getId() + ", 0)");
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }

    private void addUser(GuildJoinEvent event, Member member) {

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            Guild g = event.getGuild();

            ResultSet guildId = statement.executeQuery("SELECT GuildId FROM guild WHERE DiscordId='" + g.getId() + "'");
            guildId.next();
            int discordId = guildId.getInt("GuildId");

            ResultSet check = statement.executeQuery("SELECT COUNT(*) > 0 AS UserAlreadyExists FROM users WHERE UserDiscordId='" + member.getId() + "'");
            check.next();
            boolean UserAlreadyExists = check.getBoolean("UserAlreadyExists");


            if (!UserAlreadyExists) {
                statement.execute("INSERT INTO users (UserDiscordId) VALUES('" + member.getId() + "')");
            }


            ResultSet UserId = statement.executeQuery("SELECT UserId FROM users WHERE UserDiscordId='" + member.getId() + "'");
            UserId.next();
            int userId = UserId.getInt("UserId");

            ResultSet check2 = statement.executeQuery("SELECT COUNT(*) > 0 AS DiscordAndUserExistsInXp FROM xp WHERE GuildId='" + discordId + "' AND UserId='" + userId + "'");
            check2.next();
            boolean DiscordAndUserExistsInXp = check2.getBoolean("DiscordAndUserExistsInXp");


            if (!DiscordAndUserExistsInXp) {
                statement.execute("INSERT INTO xp (GuildId, UserId) " + "VALUES('" + discordId + "', '" + userId + "')");
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }
}
