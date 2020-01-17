package me.stqlth.krypto.commands.music;


import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import me.stqlth.krypto.music.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import static java.lang.Integer.parseInt;

public class SkipTo extends Command {

    private MusicMessages musicMessages;
    public SkipTo(MusicMessages musicMessages) {
        this.name = "skipto";
        this.help = "Skip to a certain position in the queue.";
        this.guildOnly = true;
        this.category = new Category("Music");
        this.arguments = "<song number>";

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
        TrackScheduler scheduler = musicManager.scheduler;
        ArrayList<AudioTrack> tracks = new ArrayList<>(queue);
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (!audioManager.isConnected()) {
            musicMessages.botNotConnected(channel, event);
            return;
        }
        if (!memberVoiceState.inVoiceChannel()) {
            musicMessages.userNotConnected(channel, event);
            return;
        }
        if (musicManager.scheduler.getQueue().isEmpty()) {
            musicMessages.emptyQueue(channel, event);
            return;
        }
        if (args.length <= 1) {
            musicMessages.notEnoughSkipToArgs(channel, event);
            return;
        }

        try {
            int goTo = parseInt(args[1]) - 1;
            int trackSize = tracks.size();

        for (int i = 0; i < trackSize; i++) {
            if (i == goTo) {
                scheduler.nextTrack();
                break;
            } else {
                scheduler.nextTrack();
            }
        }
        } catch (IndexOutOfBoundsException e) {
            musicMessages.invalidOneLocation(channel, event);
            return;
        }
        musicMessages.skippedTo(channel, event);
    }
}
