package me.stqlth.krypto.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.StaffMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class UnBan extends Command {
    private StaffMessages staffMessages;

    public UnBan(StaffMessages staffMessages) {
        this.name = "unban";
        this.help = "Unban a user.";
        this.guildOnly = true;
        this.hidden = true;
        this.staffMessages = staffMessages;
    }

    @Override
    protected void execute(CommandEvent event) {//Needs some testing a bug fixing. Perhaps look at recoding this

        TextChannel channel = event.getTextChannel();
        TextChannel logChannel = event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName()
                .toLowerCase().contains("staff-log")).findFirst().orElse(null);

        String[] args = event.getMessage().getContentRaw().split(" ");
        Message message = event.getMessage();
        Member sender = event.getMember();
        Permission req = Permission.BAN_MEMBERS;

        message.delete().queue();

        if (!sender.hasPermission(req)) {
            staffMessages.noPerm(channel);
            return;
        }
        if (args.length < 3) {
            staffMessages.sendErrorMessage(channel, sender, event, getName());
            return;
        }
        String argsJoined = String.join(" ", args[1]);
        event.getGuild().retrieveBanList().queue((bans -> {
            List<User> goodUsers = bans.stream().filter(ban -> isCorrectUser(ban, argsJoined))
                    .map(Guild.Ban::getUser).collect(Collectors.toList());

            if (goodUsers.isEmpty()) {
                staffMessages.notBanned(channel);
                return;
            }

            if (goodUsers.size() > 1) {
                staffMessages.multipleBan(channel, sender, event);
                return;
            }
            StringBuilder reason = new StringBuilder();
            for (int i = 2; i < args.length; i++) reason.append(args[i]).append(" ");

            User target = goodUsers.get(0);
            String mod = String.format("%#s", event.getAuthor());

            event.getGuild().unban(target).reason("Unbanned By " + mod).queue();
            if (logChannel != null) staffMessages.unBanLog(target, sender, reason.toString(), channel, logChannel);

        }));


    }

    private boolean isCorrectUser(Guild.Ban ban, String args) {
        User bannedUser = ban.getUser();

        return bannedUser.getName().equalsIgnoreCase(args) || bannedUser.getId().equals(args)
                || String.format("%#s", bannedUser).equalsIgnoreCase(args);
    }

}
