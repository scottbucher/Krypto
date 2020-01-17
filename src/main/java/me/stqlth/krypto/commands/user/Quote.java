package me.stqlth.krypto.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.QuoteMessages;
import net.dv8tion.jda.api.entities.TextChannel;

public class Quote extends Command {

    private QuoteMessages quoteMessages;

    public Quote(QuoteMessages quoteMessages) {
        this.name = "quote";
        this.help = "Quote a message.";
        this.guildOnly = true;
        this.category = new Category("Fun");
        this.arguments = "<message ID>";

        this.quoteMessages = quoteMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = event.getTextChannel();

        if (args.length <= 1) {
            quoteMessages.invalidUsage(channel);
            return;
        }

        TextChannel quoteChannel = event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName()
                .toLowerCase().contains("quotes")).findFirst().orElse(null);

        if (quoteChannel == null) {
            quoteMessages.noQuoteChannel(channel);
            return;
        }


        try {
            channel.retrieveMessageById(args[1]).submit().thenAccept((message) -> {

                quoteMessages.messageQuote(quoteChannel, message.getContentStripped(), event.getMember(), message.getAuthor());
            }).whenComplete((s, error) -> {
                if (error != null)
                    quoteMessages.couldNotFindMessage(channel, args[1]);
            });
        } catch (Exception e) {
            quoteMessages.invalidUsage(channel);
        }

        event.getMessage().delete().queue();
    }
}
