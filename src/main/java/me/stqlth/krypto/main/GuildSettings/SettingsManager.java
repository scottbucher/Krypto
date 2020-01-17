package me.stqlth.krypto.main.GuildSettings;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;

public class SettingsManager implements GuildSettingsManager<GuildSettingsProvider> {

    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;

    public SettingsManager(KryptoConfig kryptoConfig, DebugMessages debugMessages) {
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
    }

    @Override
    public GuildSettingsProvider getSettings(Guild guild) {
        return new me.stqlth.krypto.main.GuildSettings.Settings(guild, kryptoConfig, debugMessages);
    }

}
