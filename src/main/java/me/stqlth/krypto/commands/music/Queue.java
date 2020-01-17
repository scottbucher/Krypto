package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class Queue extends Command {

    private GetMessageInfo getMessageInfo;
    private MusicMessages musicMessages;
    public Queue(GetMessageInfo getMessageInfo, MusicMessages musicMessages)
    {
        this.name = "queue";
        this.help = "Display the queue.";
        this.guildOnly = true;
        this.category = new Category("Music");

        this.getMessageInfo = getMessageInfo;
        this.musicMessages = musicMessages;
    }

    @Override
    protected void execute(CommandEvent event) {
        TextChannel channel = event.getTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance(musicMessages);
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (queue.isEmpty()) {
            musicMessages.queueIsEmpty(channel, event);
            return;
        }

        int trackCount = Math.min(queue.size(), 10);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Current Queue** (Total: " + queue.size() + " tracks)");


        AudioTrackInfo currentTrackInfo = musicManager.player.getPlayingTrack().getInfo();
            builder.setDescription("\uD83C\uDFB5 __**Now Playing**__\n" + currentTrackInfo.title + " | " + currentTrackInfo.author +
                "\n\n __**Up Next**__ \u23ED\n")
            .setColor(Color.decode("#42f4f4"));

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    "`"+(i+1)+":` [%s]("+info.uri+") - %s\n\n",
                    info.title,
                    info.author
            ));
        }
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        builder.setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.",botIcon);
        channel.sendMessage(builder.build()).queue();

    }

}
