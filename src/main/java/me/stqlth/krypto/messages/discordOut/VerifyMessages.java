package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class VerifyMessages {

        public void roleVerifySetupMessage(CommandEvent event, TextChannel rolecall) {
            EmbedBuilder builder = new EmbedBuilder();
            SelfUser bot = event.getJDA().getSelfUser();
            String botIcon = bot.getAvatarUrl();

            builder.setTitle("Role Manager")
                    .setColor(Color.decode("#00ff80"))
                    .addField("Use this to obtain your roles.", "React with emotes to the corresponding activities you partake in", false)
                    .setFooter("To leave a role, simply remove your reaction of the corresponding role.", botIcon)
                    .addField("Games \uD83C\uDFAE\nPokémon \uD83E\uDD5A\nRolePlay  \uD83C\uDFAD\n" +
                            "Animals \uD83D\uDC15\nLeveling \u2B50\nPolls \uD83D\uDCCA\nMovieNight " +
                            "\uD83C\uDF7F\nStream \uD83D\uDCFA\nNSFW \u26A0\nMusic \uD83C\uDFB5", "", false)
                    .addField("Role Selection", "If there is not a role category that you enjoy contact Stqlth#0001 to request it.", false);

            rolecall.sendMessage("@everyone").queue();
            rolecall.sendMessage(builder.build()).queue(result -> {
                result.addReaction("\uD83C\uDFAE").queue();//Games
                result.addReaction("\uD83E\uDD5A").queue();//Pokémon
                result.addReaction("\uD83C\uDFAD").queue();//RolePlay
                result.addReaction("\uD83D\uDC15").queue();//Animals
                result.addReaction("\u2B50").queue();//Leveling
                result.addReaction("\uD83D\uDCCA").queue();//Polls
                result.addReaction("\uD83C\uDF7F").queue();//MovieNight
                result.addReaction("\uD83D\uDCFA").queue();//Stream
                result.addReaction("\u26A0").queue();//NSFW
                result.addReaction("\uD83C\uDFB5").queue();//Music
            });
        }
}
