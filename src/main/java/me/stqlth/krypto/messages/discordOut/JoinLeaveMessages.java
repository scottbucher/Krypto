package me.stqlth.krypto.messages.discordOut;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

public class JoinLeaveMessages extends ListenerAdapter {

    public void welcomeMessage(GuildMemberJoinEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        User user = event.getUser();

        TextChannel channel = event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("role-call")).findFirst().orElse(null);
        if (channel == null) return;

        builder.setTitle("**Welcome to the Kryptonian Discord!**")
            .setDescription("Vist " + channel.getAsMention() + " to select yours roles " + event.getMember().getUser().getName())
            .setColor( Color.decode("#34f922"))
            .setFooter("Enjoy your stay!", user.getAvatarUrl());
        TextChannel target = event.getGuild().getTextChannels().stream().filter(tchannel -> tchannel.getName().toLowerCase().contains("join-leave")).findFirst().orElse(null);

        if (target != null)
            Objects.requireNonNull(event.getGuild().getTextChannelById(target.getId())).sendMessage(builder.build()).queue();
    }

    public void leaveMessage(GuildMemberLeaveEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        User user = event.getUser();

        TextChannel channel = event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("role-call")).findFirst().orElse(null);
        if (channel == null) return;

        builder.setDescription("Goodbye " + user.getAsTag() + "!")
                .setColor( Color.decode("#EA2027"));
        TextChannel target = event.getGuild().getTextChannels().stream().filter(tchannel -> tchannel.getName().toLowerCase().contains("join-leave")).findFirst().orElse(null);

        if (target != null)
            Objects.requireNonNull(event.getGuild().getTextChannelById(target.getId())).sendMessage(builder.build()).queue();
    }
}
