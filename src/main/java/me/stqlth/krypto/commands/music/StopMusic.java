package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopMusic extends Command {

    private MusicMessages musicMessages;
    public StopMusic(MusicMessages musicMessages)
    {
        this.name = "stop";
        this.help = "Stop the current song.";
        this.guildOnly = true;
        this.category = new Category("Music");

        this.musicMessages = musicMessages;
    }

    @Override
    protected void execute(CommandEvent event) {
        TextChannel channel = event.getTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance(musicMessages);
        AudioManager audioManager = event.getGuild().getAudioManager();
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        if (!audioManager.isConnected()) {
            musicMessages.botNotConnected(channel, event);
            return;
        }
        if (!memberVoiceState.inVoiceChannel()) {
            musicMessages.userNotConnected(channel, event);
            return;
        }


        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        audioManager.closeAudioConnection();

        musicMessages.stopCommandSuccess(event.getTextChannel(), event);

    }
}
