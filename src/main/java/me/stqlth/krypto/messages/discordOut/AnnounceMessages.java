package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;
import java.awt.*;

public class AnnounceMessages {

    public void couldNotFindChannel(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Specified channel not found");
        channel.sendMessage(builder.build()).queue();
    }

    public void noPerm(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("You don't have permission to post an announcement.");
        channel.sendMessage(builder.build()).queue();
    }

    public void invalidUsage(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Please use the correct usage:\nannounce <message> <#channel> <#hex color> <thumbnail url> <@mention>");
        channel.sendMessage(builder.build()).queue();
    }

    public void announcementCreated(TextChannel channel, TextChannel pollChannel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Announcement created in " + pollChannel.getAsMention());
        channel.sendMessage(builder.build()).queue();
    }

    public void createAnnouncement(CommandEvent event, String announcement, TextChannel announcementChannel,
                                   @Nullable Color hexColor, @Nullable String thumbnailLink) {

        EmbedBuilder builder = new EmbedBuilder();
        SelfUser bot = event.getJDA().getSelfUser();

        builder.setTitle("__**Announcement**__")
                .setFooter("Announcement made by " + event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());

        if (hexColor != null)
            builder.setColor(hexColor);
         else builder.setColor(Color.decode("#42f4f4"));

        if (thumbnailLink != null)
            builder.setThumbnail(thumbnailLink);

        builder.setDescription(announcement);

        announcementChannel.sendMessage(builder.build()).queue();

    }
}
