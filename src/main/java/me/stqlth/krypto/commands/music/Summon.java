package me.stqlth.krypto.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Summon extends Command {

    private GetMessageInfo getMessageInfo;
    private MusicMessages musicMessages;
    public Summon(GetMessageInfo getMessageInfo, MusicMessages musicMessages)
    {
        this.name = "summon";
        this.help = "Bring Krypto into your voice channel.";
        this.guildOnly = true;
        this.category = new Category("Music");

        this.getMessageInfo = getMessageInfo;
        this.musicMessages = musicMessages;
    }


    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            String field = "The bot is already in another channel.";
            String footer = "Please wait until the bot is no longer in use or use the `" + getMessageInfo.getPrefix(event.getGuild()) + "summon` command";
            sendErrorMessage(channel, event.getMember(), footer, field, event);
            return;
        }

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            String field = "You are not in a channel";
            String footer = "Please join a channel and then use this command.";
            sendErrorMessage(channel, event.getMember(), footer, field, event);
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfMember = event.getSelfMember();

        if (voiceChannel == null) {
            musicMessages.userNotConnected(channel, event);
            return;
        }

        if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            String field = "I do not have permission to connect to this channel.";
            String footer = "Please contact an administrator to correct this if this is an error.";
            sendErrorMessage(channel, event.getMember(), footer, field, event);
            return;
        }
        audioManager.openAudioConnection(voiceChannel);
        joinCommandSuccess(channel, event.getMember(), event);

    }












    public void sendErrorMessage(TextChannel channel, Member member, String footer, String field, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setFooter(footer, botIcon)
                .setDescription(field);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);

    }

    public void joinCommandSuccess(TextChannel channel, Member member, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Connecting...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("I am connecting to your voice channel now.")
                .setFooter("Use the command `" + getMessageInfo.getPrefix(event.getGuild()) + "play` to play music.",botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);

    }
}
