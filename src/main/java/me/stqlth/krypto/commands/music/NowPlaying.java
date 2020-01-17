package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class NowPlaying extends Command {

    private MusicMessages musicMessages;
    public NowPlaying(MusicMessages musicMessages)
    {
        this.name = "np";
        this.help = "Show the currently playing track.";
        this.guildOnly = true;
        this.category = new Category("Music");

        this.musicMessages = musicMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance(musicMessages);
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        List<AudioTrack> tracks = new ArrayList<>(queue);

        if (player.getPlayingTrack() == null) {
            musicMessages.noCurrentSong(channel, event);
            return;
        }

        AudioTrackInfo currentTrack = player.getPlayingTrack().getInfo();

        if (tracks.isEmpty()) {
            musicMessages.nowPlayingSong(channel, event, currentTrack, player);
            return;
        }

        AudioTrackInfo nextTrack = tracks.get(0).getInfo();
        musicMessages.nowPlayingSong(channel, event, currentTrack, nextTrack, player);
    }


}
