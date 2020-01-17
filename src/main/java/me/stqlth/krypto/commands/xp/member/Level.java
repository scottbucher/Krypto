package me.stqlth.krypto.commands.xp.member;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.XpMessages;
import net.dv8tion.jda.api.entities.Member;

public class Level extends Command {
    private XpMessages xpMessages;

    public Level(XpMessages xpMessages) {
        this.name = "level";
        this.aliases = new String[]{"xp"};
        this.help = "View your level.";
        this.guildOnly = true;
        this.category = new Category("Tools");
        this.xpMessages = xpMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length == 1) {
            xpMessages.xpMessage(event.getTextChannel(), event);
            return;
        }

        Member target = event.getMessage().getMentionedMembers().get(0);

        if (target.getUser().isBot()) return;

        xpMessages.xpMessage(event.getTextChannel(), target, event);

    }


}
