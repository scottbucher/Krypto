package me.stqlth.krypto.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.AnnounceMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Announce extends Command {

    private AnnounceMessages announceMessages;

    public Announce(AnnounceMessages announceMessages) {
        this.name = "announce";
        this.arguments = "<message> <channel> <hex color> <thumbnail url> <mention>";
        this.help = "Post an announcement.";
        this.guildOnly = true;
        this.hidden = true;
        this.category = new Category("Fun");

        this.announceMessages = announceMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = event.getTextChannel();

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            announceMessages.noPerm(channel);
            return;
        }

        if (args.length <= 3) {
            announceMessages.invalidUsage(channel);
            return;
        }

        StringBuilder argument = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            argument.append(" ").append(args[i]);
        }
        String[] split = argument.toString().split(";");

//        Arrays.stream(split).forEach(System.out::println);

        TextChannel announceChannel;
        try {
            announceChannel = event.getMessage().getMentionedChannels().get(0);
        } catch (IndexOutOfBoundsException e) {
            announceMessages.couldNotFindChannel(channel);
            return;
        }

        String announcement = split[0];
        String optional1 = null;
        String optional2 = null;
        String optional3 = null;
        String atGroup = null;
        Color hexColor = null;
        String link = null;

        if (split.length == 5) {
            optional1 = split[2];
            optional2 = split[3];
            optional3 = split[4];
        } else if (split.length == 4) {
            optional1 = split[2];
            optional2 = split[3];
        } else if (split.length == 3)
            optional1 = split[2];


        Role role = null;
        if (optional1 != null) {

            try {
                hexColor = Color.decode(optional1);
            } catch (IllegalArgumentException ignored) {
            }

            if (hexColor == null) {
                if (isUrl(optional1)) {
                    link = optional1;
                } else if (optional1.toLowerCase().equals("everyone")) {
                    atGroup = optional1;
                } else if (optional1.toLowerCase().equals("here")) {
                    atGroup = optional1;
                } else {
                    String finalOptional = optional1;
                    role = event.getGuild().getRoles().stream().filter(role1 -> role1.getName().toLowerCase()
                            .contains(finalOptional.toLowerCase())).findFirst().orElse(null);
                }

            }
        }

        if (optional2 != null) {
            if (isUrl(optional2)) {
                link = optional2;
            } else if (optional2.toLowerCase().equals("everyone")) {
                atGroup = optional2;
            } else if (optional2.toLowerCase().equals("here")) {
                atGroup = optional2;
            } else {
                String finalOptiona2 = optional2;
                role = event.getGuild().getRoles().stream().filter(role1 -> role1.getName().toLowerCase()
                        .contains(finalOptiona2.toLowerCase())).findFirst().orElse(null);
            }
        }


        if (optional3 != null) {

            if (atGroup == null) {
                if (optional3.toLowerCase().equals("everyone")) {
                    atGroup = optional3;
                } else if (optional3.toLowerCase().equals("here")) {
                    atGroup = optional3;
                } else {
                    String finalOptiona3 = optional3;
                    role = event.getGuild().getRoles().stream().filter(role1 -> role1.getName().toLowerCase()
                            .contains(finalOptiona3.toLowerCase())).findFirst().orElse(null);
                }
            }
        }

        if (atGroup != null) {
            if (atGroup.equals("everyone"))
                announceChannel.sendMessage("@everyone").queue();
            else if (atGroup.equals("here"))
                announceChannel.sendMessage("@here").queue();
        } else if (role != null) {
            announceChannel.sendMessage(role.getAsMention()).queue();
        }

        announceMessages.createAnnouncement(event, announcement, announceChannel, hexColor, link);
        announceMessages.announcementCreated(channel, announceChannel);
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }
}
