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

            ResultSet check = statement.executeQuery("CALL DoesGuildAlreadyExist(" + guild.getId() + ")");
            check.next();
            boolean alreadyExists = check.getBoolean("AlreadyExists");

            if (!alreadyExists) {
                statement.execute("CALL InsertGuildSettings('!')");
                ResultSet rs = statement.executeQuery("CALL SelectLastInsertID()");
                rs.next();
                int lastId = rs.getInt("LAST_INSERT_ID()");
                statement.execute("CALL InsertGuild(" + guild.getId() + ", " + lastId + ", 1)");
            } else {
                statement.execute("CALL UpdateGuildActive(" + guild.getId() + ", 1)");
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

            ResultSet guildId = statement.executeQuery("CALL GetGuildId(" + g.getId() + ")");
            guildId.next();
            int discordId = guildId.getInt("GuildId");

            ResultSet check = statement.executeQuery("CALL DoesUserAlreadyExist(" + member.getId() + ")");
            check.next();
            boolean UserAlreadyExists = check.getBoolean("AlreadyExists");

            if (!UserAlreadyExists) {
                statement.execute("CALL InsertUser(" + member.getId() + ")");
            }

            ResultSet UserId = statement.executeQuery("CALL GetUserId(" + member.getId() + ")");
            UserId.next();
            int userId = UserId.getInt("UserId");

            ResultSet check2 = statement.executeQuery("CALL DoesDiscordAndUserExistInXp(" + discordId + ", " + userId + ")");
            check2.next();
            boolean DiscordAndUserExistsInXp = check2.getBoolean("AlreadyExists");


            if (!DiscordAndUserExistsInXp) {
                statement.execute("CALL InsertXp(" + discordId + ", " + userId + ")");
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }
}
