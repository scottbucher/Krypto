package me.stqlth.krypto.messages.discordOut;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class EightBallMessages {

    public EightBallMessages() {
    }

    public void notAQuestion(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Shouldn't you have learned proper punctuation in school?")
                .setColor(Color.decode("#EA2027"));

        channel.sendMessage(builder.build()).queue();
    }

    public void noYes(TextChannel channel, boolean noYes) {
        EmbedBuilder builder = new EmbedBuilder();

        if (noYes) {
            builder.setDescription("Yes")
                    .setColor(Color.decode("#34f922"));
        } else {
            builder.setDescription("No")
                    .setColor(Color.decode("#EA2027"));
        }

        channel.sendMessage(builder.build()).queue();
    }

    public void reply(TextChannel channel, String reply) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setDescription(reply)
                .setColor(Color.CYAN);

        channel.sendMessage(builder.build()).queue();
    }
}
