package me.stqlth.krypto.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class Hello extends Command
{
    private final EventWaiter waiter;
    public Hello(EventWaiter waiter)
    {
        this.waiter = waiter;
        this.name = "hello";
        this.help = "Say hello to Krypto!";
        this.guildOnly = false;
        this.category = new Category("Info");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        // ask what the user's name is
        event.reply("Hello. What is your name?");

        // wait for a response
        waiter.waitForEvent(MessageReceivedEvent.class,
                // make sure it's by the same user, and in the same channel
                e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
                // respond, inserting the name they listed into the response
                e -> event.reply("Hello, `"+e.getMessage().getContentRaw()+"`! I'm `"+e.getJDA().getSelfUser().getName()+"`!"),
                // if the user takes more than a minute, time out
                1, TimeUnit.MINUTES, () -> event.reply("Sorry, you took too long."));
    }
}
