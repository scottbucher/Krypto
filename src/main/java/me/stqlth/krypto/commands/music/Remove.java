package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import static java.lang.Integer.parseInt;

public class Remove extends Command {

    private MusicMessages musicMessages;
    public Remove(MusicMessages musicMessages) {
        this.name = "remove";
        this.help = "Remove a song from the queue.";
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
            musicMessages.notEnoughRemoveArgs(channel, event);
            return;
        }

        try {
            AudioTrack item = tracks.get(parseInt(args[1]) - 1);
            tracks.remove(item);
        } catch (IndexOutOfBoundsException e) {
            musicMessages.invalidOneLocation(channel, event);
            return;
        }

        musicManager.scheduler.getQueue().clear();
        tracks.forEach(musicManager.scheduler::queue);
        musicMessages.removedSong(channel, event);
    }
}
