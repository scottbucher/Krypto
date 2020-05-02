package me.stqlth.krypto.events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GuildMessageReceived extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw().toLowerCase();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();

        if (message.contains("good morning") || message.contains("good mornin")) {
            channel.sendMessage("Good Morning " + Objects.requireNonNull(member).getAsMention() + "!").queue();
        }
        if (message.contains("good night")) {
            channel.sendMessage("Good Night " + Objects.requireNonNull(member).getAsMention() + "!").queue();
        }

    }
}
