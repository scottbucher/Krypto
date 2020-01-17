package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Replay extends Command {

    private MusicMessages musicMessages;
    public Replay(MusicMessages musicMessages) {
        this.name = "replay";
        this.help = "Starts the current song from the beginning.";
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
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        ArrayList<AudioTrack> tracks = new ArrayList<>(queue);
        AudioPlayer player = musicManager.player;

        if (!audioManager.isConnected()) {
            musicMessages.botNotConnected(channel, event);
            return;
        }
        if (!memberVoiceState.inVoiceChannel()) {
            musicMessages.userNotConnected(channel, event);
            return;
        }
        if (musicManager.player.getPlayingTrack() == null) {
            musicMessages.noCurrentSong(channel, event);
            return;
        }

        player.getPlayingTrack().setPosition(0);
        if(player.isPaused()) player.setPaused(false);
        musicMessages.replaySong(channel, event);
    }
}
