package me.stqlth.krypto.main.GuildSettings;

import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

public class Settings implements GuildSettingsProvider {

    private final Guild guild;
    private Collection<String> prefixes;
    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;

    public Settings(Guild guild, KryptoConfig kryptoConfig, DebugMessages debugMessages) {
        this.guild = guild;
        this.prefixes = new LinkedList<>();
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
    }

    @Override
    public Collection<String> getPrefixes() {


        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            int gSettingsId=0;

            ResultSet id = statement.executeQuery("SELECT GuildSettingsId FROM guild WHERE DiscordId='" + this.guild.getId() + "'");
            if (id.next()) gSettingsId = id.getInt("GuildSettingsId");

            ResultSet rs = statement.executeQuery("SELECT Prefix FROM guildsettings WHERE GuildSettingsId='" + gSettingsId + "'");

            if (rs.next()) {
                prefixes.add(rs.getString("Prefix"));
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
        return prefixes;
    }
}
