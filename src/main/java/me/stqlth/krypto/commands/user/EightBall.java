package me.stqlth.krypto.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.EightBallMessages;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class EightBall extends Command {

    private EightBallMessages eightBallMessages;

    public EightBall(EightBallMessages eightBallMessages) {
        this.name = "8ball";
        this.aliases = new String[]{"eightball"};
        this.help = "Ask the magic 8 ball a question.";
        this.guildOnly = true;
        this.category = new Category("Fun");
        this.arguments = "<question>";

        this.eightBallMessages = eightBallMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();

        if (!event.getMessage().getContentRaw().contains("?")) {
            eightBallMessages.notAQuestion(channel);
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args[1].equalsIgnoreCase("yes?")) {
            eightBallMessages.noYes(channel, false);
            return;
        } else if (args[1].equalsIgnoreCase("no?")) {
            eightBallMessages.noYes(channel, true);
            return;
        }

        List<String> replies = Arrays.asList(
                "Yes",
                "No",
                "It is certain",
                "It is decidedly so",
                "Without a doubt",
                "Yes definitely",
                "You may rely on it",
                "As I see it, yes",
                "Most likely",
                "Outlook good",
                "Signs point to yes",
                "Reply hazy try again",
                "Ask again later",
                "Better not tell you now",
                "Cannot predict now",
                "Concentrate and ask again",
                "Don't count on it",
                "My reply is no",
                "My sources say no",
                "Outlook not so good",
                "Honestly? I couldn't give less of a shit",
                "One sec I've got shit dripping down my leg",
                "Very doubtful",
                "Signs point to yes. Except that you were born an idiot, you will die an idiot, and nothing will change in-between.",
                "Without a doubt. Nah, I’m just messing with you, go shoot yourself.",
                "My sources say no. They also tell me they hate you and hope you burn in hell.",
                "Yes, definitely. Unless it doesn’t happen. Listen it’s not my fault your father didn’t love you. Get off my back!",
                "Outlook not so good. Especially since you’re so goddamn ugly.",
                "All signs point to yes. But on second thought, go fuck yourself.",
                "As If",
                "Ask me if I care",
                "Dumb question ask another",
                "Forget about it",
                "Get a clue",
                "In your dreams",
                "Not a chance",
                "Obviously",
                "Oh please",
                "Sure",
                "That's ridiculous",
                "Well maybe",
                "What do you think?",
                "Yes... you prick",
                "Who cares?",
                "Yeah and I'm the fucking Pope",
                "Yeah right",
                "You wish",
                "You've got to be kidding...");

        String randomReply = replies.get((int) Math.floor(Math.random() * replies.size()));
        eightBallMessages.reply(channel, randomReply);
    }
}
