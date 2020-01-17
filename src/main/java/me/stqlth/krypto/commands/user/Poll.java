package me.stqlth.krypto.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.PollMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;


public class Poll extends Command {

    private PollMessages pollMessages;
    public Poll(PollMessages pollMessages) {
        this.name = "poll";
        this.help = "Create a poll.";
        this.guildOnly = true;
        this.category = new Category("Tools");
        this.arguments = "create <question> <option> <option>...";

        this.pollMessages = pollMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = event.getTextChannel();
        Guild guild = event.getGuild();

        if (args.length <= 1) {
            pollMessages.invalidUsage(channel);
            return;
        }

        String argument = "";
        for (int i = 2; i < args.length; i++) {
            argument += " " + args[i];
        }
        String[] split = argument.split(";");

        if (split.length < 3) {
            pollMessages.notEnoughOptions(channel);
            return;
        }

        TextChannel pollChannel = guild.getTextChannels().stream().filter(channel1 -> channel1.getName().toLowerCase().contains("polls")).findFirst().orElse(null);
        if (pollChannel == null) {
            pollMessages.noPollChannel(channel);
            return;
        }
        ArrayList<String> Options = new ArrayList<>();
        for(int i = 1; i < split.length; i++) {
            Options.add(split[i]);
        }

        if (Options.size() > 11) {
            pollMessages.tooManyOptions(channel);
            return;
        }

        //send poll
        pollMessages.createPoll(event, pollChannel, split[0], Options);
        pollMessages.pollCreated(channel, pollChannel);
    }
}
