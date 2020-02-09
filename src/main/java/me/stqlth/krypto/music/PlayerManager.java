package me.stqlth.krypto.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private MusicMessages musicMessages;

    private PlayerManager(MusicMessages musicMessages) {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        this.musicMessages = musicMessages;
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, CommandEvent event) {
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        AudioManager audioManager = event.getGuild().getAudioManager();
        assert memberVoiceState != null;
        if (!memberVoiceState.inVoiceChannel()) {
            String field = "You are not in a channel";
            String footer = "Please join a channel and then use this command.";
            sendErrorMessage(event.getTextChannel(), event.getMember(), footer, field, event);
            return;
        }

        if (!audioManager.isConnected()) {
            VoiceChannel voiceChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(voiceChannel);
        } else if(!Objects.equals(memberVoiceState.getChannel(), audioManager.getConnectedChannel())) {
            String field = "You are not in the same channel as the bot";
            String footer = "Please join a channel and then use this command.";
            sendErrorMessage(event.getTextChannel(), event.getMember(), footer, field, event);
            return;
        }

        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                musicMessages.addSingleToQueue(channel, track, event);

                play(musicManager, track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }
                musicMessages.addPlayListToQueue(channel, firstTrack, playlist, event);

                playlist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                musicMessages.noMatch(channel, event);
            }

            @Override
            public void loadFailed(FriendlyException e) {
                musicMessages.loadFailed(channel, e, event);
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance(MusicMessages musicMessages) {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager(musicMessages);
        }
        return INSTANCE;
    }

    public static void sendErrorMessage(TextChannel channel, Member member, String footer, String field, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Action Can't be completed")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setFooter(footer, botIcon)
                .setDescription(field);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
    }
}
