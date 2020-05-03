package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class VerifyMessages {
    public void roleVerifySetupMessage(CommandEvent event, TextChannel roleCall) {
        EmbedBuilder builder = new EmbedBuilder();
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();

        builder.setTitle("Role Manager")
            .setColor(Color.decode("#00ff80"))
            .addField("Use this to obtain your roles.", "React with emotes to the corresponding activities you partake in", false)
            .setFooter("To leave a role, simply remove your reaction of the corresponding role.", botIcon)
            .addField("Topics", "😂 Memes\n🎮 Games\n🎵 Music\n💢 Anime\n🐱 Animals\n🛑 NNSFW\n🎭 Roleplay", false)
            .addField("Games", "⛏ Minecraft\n🍂 Animal Crossing\n🥚 Pokemon\n⚔ Runescape", false)
            .addField("Events", "🍿 Movie Streams\n👾 Game Streams", false)
            .addField("Role Selection", "If there is not a role category that you enjoy contact Stqlth#0001 to request it.", false);

        roleCall.sendMessage(builder.build()).queue(message -> {
            String[] reactions = new String[] {"😂", "🎮", "🎵", "💢", "🐱", "🛑", "🎭", "⛏", "🍂", "🥚", "⚔", "🍿", "👾"};
            for (String reaction : reactions) 
            { 
                this.addReaction(message, reaction);
            }
        });
    }

    private void addReaction(Message message, String reaction) {
        message.addReaction(reaction).queue();
    }
}