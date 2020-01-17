package me.stqlth.krypto.messages.discordOut;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class QuoteMessages {

    public void noQuoteChannel(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("No quote channel found!");
        channel.sendMessage(builder.build()).queue();
    }

    public void couldNotFindMessage(TextChannel channel, String quoteId) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Could not find a message in " + channel.getAsMention() + " with the ID: " + quoteId);
        channel.sendMessage(builder.build()).queue();
    }

    public void invalidUsage(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Please provide a proper message ID. (Right Click on message -> Copy Id)");
        channel.sendMessage(builder.build()).queue();
    }

    public void messageQuote(TextChannel quoteChannel, String quote, Member quoter, User sender) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#42f4f4"))
                .setTitle("Quote From " + sender.getName())
                .setThumbnail(sender.getAvatarUrl())
                .setDescription(quote)
                .setFooter("Quoted by " + quoter.getEffectiveName(), quoter.getUser().getAvatarUrl());
        quoteChannel.sendMessage(builder.build()).queue();
    }
}
