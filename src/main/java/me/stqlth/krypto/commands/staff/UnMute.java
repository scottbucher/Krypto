package me.stqlth.krypto.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.StaffMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class UnMute extends Command {
    private StaffMessages staffMessages;

    public UnMute(StaffMessages staffMessages) {
        this.name = "unmute";
        this.help = "Unmute a user.";
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
        Permission req = Permission.VOICE_MUTE_OTHERS;
        Role mutedRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("muted")).findFirst().orElse(null);

        message.delete().queue();


        if (mutedRole == null) {
            staffMessages.noMutedRole(channel);
            return;
        }
        if (!sender.hasPermission(req)) {
            staffMessages.noPerm(channel);
            return;
        }
        if (members.isEmpty() || args.length < 3) {
            staffMessages.sendErrorMessage(channel, sender, event, getName());
            return;
        }

        Member target = members.get(0);

        if (target.getRoles().stream().filter(mRole -> mRole.getName()
                .toLowerCase().contains("muted")).findFirst().orElse(null) != null) {

            StringBuilder reason = new StringBuilder();
            for (int i = 2; i < args.length; i++) reason.append(args[i]).append(" ");

            event.getGuild().removeRoleFromMember(target, mutedRole).queue();

            if (logChannel != null) staffMessages.unMuteLog(target, sender, reason.toString(), channel, logChannel);

        }
    }
}
