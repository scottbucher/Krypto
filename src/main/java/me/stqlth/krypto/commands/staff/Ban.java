package me.stqlth.krypto.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.StaffMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Ban extends Command {
    private StaffMessages staffMessages;

    public Ban(StaffMessages staffMessages) {
        this.name = "ban";
        this.help = "Ban a user.";
        this.guildOnly = true;
        this.hidden = true;
        this.staffMessages = staffMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();
        TextChannel logChannel = event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName()
                .toLowerCase().contains("staff-log")).findFirst().orElse(null);

        String[] args = event.getMessage().getContentRaw().split(" ");
        Message message = event.getMessage();
        Member sender = event.getMember();
        List<Member> members = message.getMentionedMembers();
        Permission req = Permission.BAN_MEMBERS;

        message.delete().queue();

        if (!sender.hasPermission(req)) {
            staffMessages.noPerm(channel);
            return;
        }
        if (members.isEmpty() || args.length < 3) {
            staffMessages.sendErrorMessage(channel, sender, event, getName());
            return;
        }
        Member target = members.get(0);
        if (target.hasPermission(req)) {
            staffMessages.noPunish(channel, getName());
            return;
        }
        event.getGuild().ban(target, 1).queue();
        StringBuilder reason = new StringBuilder();

        for (int i = 2; i < args.length; i++) reason.append(args[i]).append(" ");
        if (logChannel != null) staffMessages.banLog(target, sender, reason.toString(), channel, logChannel);
    }
}

