package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import me.stqlth.krypto.music.TrackScheduler;
import net.dv8tion.jda.api.entities.TextChannel;

public class Skip extends Command {

    private MusicMessages musicMessages;
    public Skip(MusicMessages musicMessages)
    {
        this.name = "skip";
        this.help = "Skip the current song.";
        this.guildOnly = true;
        this.category = new Category("Music");

        this.musicMessages = musicMessages;
    }


    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance(musicMessages);
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            musicMessages.noCurrentSong(channel, event);
            return;
        }

        AudioTrackInfo currentTrack = musicManager.player.getPlayingTrack().getInfo();
        scheduler.nextTrack();
        AudioTrackInfo nowPlaying = musicManager.player.getPlayingTrack().getInfo();

        musicMessages.skippingTrack(channel, event, currentTrack, nowPlaying);


    }
}
