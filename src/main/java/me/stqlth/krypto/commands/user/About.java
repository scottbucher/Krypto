package me.stqlth.krypto.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;


public class About extends Command {

    private GetMessageInfo getMessageInfo;

    public About( GetMessageInfo getMessageInfo)
    {
        this.name = "about";
        this.aliases = new String[]{"botabout","hi"};
        this.help = "View information about Krypto.";
        this.category = new Category("Info");

        this.getMessageInfo = getMessageInfo;
    }

    @Override
    protected void execute(CommandEvent event) {
        Guild g = event.getGuild();

        EmbedBuilder builder = new EmbedBuilder();
        TextChannel channel = event.getTextChannel();

        builder.setDescription("Hello! I am <@634452930166259732>, a bot built by <@478288246858711040> with help from <@212772875793334272>!"+
//                "\nMy awesome logo was designed by **HunterDraxi**!" +
                "\n"+
                "\nI was written for Discord in Java, using the JDA library ("+ JDAInfo.VERSION+")"+
                "\nI'm currently in Version 1.0.0" +
                "\n"+
                "\nMy prefix for this server is `" + getMessageInfo.getPrefix(g) + "`" +
                "\nType `" + getMessageInfo.getPrefix(g) + "help` and I'll DM you a list of commands you can use!"+
//                "\nSee some of my other stats with `" + getMessageInfo.getPrefix(g) + "stats`"+
                "\n"+
                "\nFor additional help, contact <@478288246858711040>")
                .setColor(Color.decode("#00e1ff"));
        channel.sendMessage(builder.build()).queue();

    }

}
