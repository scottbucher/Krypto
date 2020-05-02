package me.stqlth.krypto.messages.getMethods;

import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.*;
import java.util.LinkedList;

public class GetMessageInfo {

    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    public GetMessageInfo(KryptoConfig kryptoConfig, DebugMessages debugMessages) {
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
    }

    public String getPrefix(Guild guild) {
        LinkedList<Object> prefixes = new LinkedList<>();

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            int gSettingsId=0;

            ResultSet id = statement.executeQuery("CALL GetGuildSettingsId(" + guild.getId() + ")");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            ResultSet rs = statement.executeQuery("CALL GetPrefix(" + gSettingsId + ")");

            if (rs.next()) {
                prefixes.add(rs.getString("Prefix"));
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return prefixes.getFirst().toString();
    }

    public String getLevelingChannelId(GuildMessageReceivedEvent event){

        LinkedList<Object> channels = new LinkedList<>();

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            int gSettingsId=0;

            ResultSet id = statement.executeQuery("CALL GetGuildSettingsId(" + event.getGuild().getId() + ")");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            ResultSet rs = statement.executeQuery("CALL GetLevelingChannel(" + gSettingsId + ")");

            if (rs.next()) {
                channels.add(rs.getString("LevelingChannel"));
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return channels.getFirst().toString();
    }

    public String getLevelingChannelId(Guild guild){

        LinkedList<Object> channels = new LinkedList<>();

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            int gSettingsId=0;

            ResultSet id = statement.executeQuery("CALL GetGuildSettingsId(" + guild.getId() + ")");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            ResultSet rs = statement.executeQuery("CALL GetLevelingChannel(" + gSettingsId + ")");

            if (rs.next()) {
                channels.add(rs.getString("LevelingChannel"));
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return channels.getFirst().toString();
    }

}
