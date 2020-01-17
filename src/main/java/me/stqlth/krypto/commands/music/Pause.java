package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Pause extends Command {

    private MusicMessages musicMessages;
    public Pause(MusicMessages musicMessages) {
        this.name = "pause";
        this.help = "Pause the current song.";
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
        AudioPlayer player = musicManager.player;

        if (!audioManager.isConnected()) {
            musicMessages.botNotConnected(channel, event);
            return;
        }

        assert memberVoiceState != null;
        if (!memberVoiceState.inVoiceChannel()) {
            musicMessages.userNotConnected(channel, event);
            return;
        }
        if (player.getPlayingTrack() == null) {
            musicMessages.noCurrentSong(channel, event);
            return;
        }


        musicManager.player.setPaused(true);
        musicMessages.pausedSong(channel, event);

    }
}
