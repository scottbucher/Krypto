package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;

public class PollMessages {

    public void noPollChannel(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("No poll channel found!");
        channel.sendMessage(builder.build()).queue();
    }

    public void notEnoughOptions(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("You need at least 2 options.");
        channel.sendMessage(builder.build()).queue();
    }

    public void tooManyOptions(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("You may only have a max of 11 options.");
        channel.sendMessage(builder.build()).queue();
    }

    public void invalidUsage(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Please use the correct usage:\npoll create <question> <option> <option>... (max 11)");
        channel.sendMessage(builder.build()).queue();
    }

    public void pollCreated(TextChannel channel, TextChannel pollChannel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("You have successfully created a new poll in " + pollChannel.getAsMention());
        channel.sendMessage(builder.build()).queue();
    }

    public void createPoll(CommandEvent event, TextChannel channel, String Question, List<String> Options) {

        EmbedBuilder builder = new EmbedBuilder();
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();

        if (!Question.contains("?")) Question += '?';

        Role pollRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("polls")).findFirst().orElse(null);

        if (pollRole != null)
            channel.sendMessage(pollRole.getAsMention()).queue();

        builder.setTitle(event.getMember().getEffectiveName() + " started a Poll!")
                .setColor(Color.decode("#42f4f4"))
                .setDescription("\n__**" + Question + "**__\n");

                for (int i = 0; i < Options.size(); i++) {
                    builder.appendDescription(numberToEmote[i] + " " + Options.get(i) + "\n");
                }

                builder.setFooter("Please react with an emote to vote in this poll.", botIcon);

        channel.sendMessage(builder.build()).queue(result -> {
            for (int i = 0; i < Options.size(); i++) {
                result.addReaction(numberToEmote[i]).queue();
            }
        });

    }

    private static final String[] numberToEmote = {
            "\u0030\u20E3",
            "\u0031\u20E3",
            "\u0032\u20E3",
            "\u0033\u20E3",
            "\u0034\u20E3",
            "\u0035\u20E3",
            "\u0036\u20E3",
            "\u0037\u20E3",
            "\u0038\u20E3",
            "\u0039\u20E3",
            "\uD83D\uDD1F"
    };
}
