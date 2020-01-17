package me.stqlth.krypto.xpSystem;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.*;

public class XpMethods {
    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;

    public XpMethods(KryptoConfig kryptoConfig, DebugMessages debugMessages) {
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
    }

    public int getGuildId(CommandEvent event) {
        int guildId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            Guild g = event.getGuild();

            ResultSet rs = statement.executeQuery("SELECT GuildId FROM guild WHERE DiscordId='" + g.getId() + "'");
            rs.next();
            guildId = rs.getInt("GuildId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return guildId;
    }

    public int getUserId(CommandEvent event) {
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT UserId FROM users WHERE UserDiscordId='" + event.getMember().getUser().getId() + "'");
            rs.next();
            userId = rs.getInt("UserId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return userId;
    }

    public int getPlayerXp(CommandEvent event) {
        int xp = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT XpAmount FROM xp WHERE GuildId='" + getGuildId(event) + "' AND UserId='" + getUserId(event) + "'");
            rs.next();
            xp = rs.getInt("XpAmount");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return xp;
    }

    public int getLevelXp(int level) {
        return ((5 * (level*level)) + (50 * level) + 100);
    }

    public int getLevelXp(CommandEvent event) {
        int level = getLevelFromXp(event);
        return ((5 * (level*level)) + (50 * level) + 100);
    }

    public int getLevelXp(CommandEvent event, Member target) {
        int level = getLevelFromXp(event, target);
        return ((5 * (level*level)) + (50 * level) + 100);
    }

    public int getLevelFromXp(CommandEvent event) {
        int remainingXp = getPlayerXp(event);
        int level = 0;

        while (remainingXp >= getLevelXp(level)) {
            remainingXp -= getLevelXp(level);
            level++;
        }
        return level;
    }

    public int getLevelFromXp(CommandEvent event, Member member) {
        int remainingXp = getPlayerXp(member, event);
        int level = 0;

        while (remainingXp >= getLevelXp(level)) {
            remainingXp -= getLevelXp(level);
            level++;
        }
        return level;
    }

    public int getLevelFromXp(int xp) {
        int remainingXp = xp;
        int level = 0;

        while (remainingXp >= getLevelXp(level)) {
            remainingXp -= getLevelXp(level);
            level++;
        }
        return level;
    }

    public int getPlayerLevelXp(int level) {
        int xp = 0;
        for(int i = 0; i < level; i++)
            xp += getLevelXp(i);
        return xp;
    }

    public int getRemainingXp(CommandEvent event) {
        return getPlayerXp(event) - getPlayerLevelXp(getLevelFromXp(event));
    }

    public int getRemainingXp(CommandEvent event, Member target) {
        return getPlayerXp(target, event) - getPlayerLevelXp(getLevelFromXp(event, target));
    }

    ////////////////////////////////////////////////////////////////////////////
    public int getUserId(Member member) {
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT UserId FROM users WHERE UserDiscordId='" + member.getUser().getId() + "'");
            rs.next();
            userId = rs.getInt("UserId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return userId;
    }

    public int getPlayerXp(Member member, CommandEvent event) {
        int xp = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT XpAmount FROM xp WHERE GuildId='" + getGuildId(event) + "' AND UserId='" + getUserId(member) + "'");
            rs.next();
            xp = rs.getInt("XpAmount");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return xp;
    }
}
