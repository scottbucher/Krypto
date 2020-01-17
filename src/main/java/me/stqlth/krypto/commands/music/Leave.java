package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import me.stqlth.krypto.music.GuildMusicManager;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Leave extends Command {

    private GetMessageInfo getMessageInfo;
    private MusicMessages musicMessages;

    public Leave(GetMessageInfo getMessageInfo, MusicMessages musicMessages) {
        this.name = "leave";
        this.help = "Remove Krypto from the voice channel.";
        this.guildOnly = true;
        this.category = new Category("Music");

        this.getMessageInfo = getMessageInfo;
        this.musicMessages = musicMessages;
    }


    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();
        PlayerManager playerManager = PlayerManager.getInstance(musicMessages);
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        if (!audioManager.isConnected()) {
            String field = "I am not in a channel!";
            String footer = "This command can only be used when the bot is in a channel.";
            sendErrorMessage(channel, event.getMember(), footer, field, event);
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            String field = "You are not in the channel!";
            String footer = "This command can only be used when the bot is in the same channel as you!";
            sendErrorMessage(channel, event.getMember(), footer, field, event);
            return;
        }

        audioManager.closeAudioConnection();
        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        leaveCommandSuccess(channel, event.getMember(), event);


    }


    public void sendErrorMessage(TextChannel channel, Member member, String footer, String field, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Action Can't be completed")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setFooter(footer, null)
                .setDescription(field);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);

    }

    public void leaveCommandSuccess(TextChannel channel, Member member, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Success!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("I have disconnected from your voice channel")
                .setFooter("Use the command `" + getMessageInfo.getPrefix(event.getGuild())  + "summon` to summon me again.", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);

    }

}