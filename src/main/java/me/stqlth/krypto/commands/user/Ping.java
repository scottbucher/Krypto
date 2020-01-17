package me.stqlth.krypto.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.TextChannel;

public class Ping extends Command {

    public Ping() {
        this.name = "ping";
        this.help = "View bot latency.";
        this.guildOnly = false;
        this.arguments = "[fancy]";
        this.category = new Category("Tools");
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = event.getTextChannel();

        if (args.length > 1 && args[1].matches("fancy")) {
            channel.sendMessage("Checking ping...").queue(message -> {
                int howManyPings = 5;
                int lastPing;
                int sum = 0, min = 999, max = 0;
                long startPing = System.currentTimeMillis();
                for (int i = 0; i < howManyPings; i++) {
                    message.editMessage(pingEmotes[i%pingEmotes.length]).queue();
                    lastPing = (int) (System.currentTimeMillis() - startPing);
                    sum += lastPing;
                    max = Math.max(max, lastPing);
                    min = Math.min(min, lastPing);
                    try {
                        Thread.sleep(1_500L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startPing = System.currentTimeMillis();
                }
                message.editMessage(String.format("Average ping is %dms (min: %d, max: %d)", (int) Math.ceil(sum / 5f), min, max)).queue();
            });
        } else {
            long startPing = System.currentTimeMillis();
            channel.sendMessage(":outbox_tray: checking ping").queue(message ->
                   message.editMessage(":inbox_tray: the ping is "
                           + (System.currentTimeMillis() - startPing) + "ms").queue());
        }
    }

    private static final String[] pingEmotes = new String[]{
            ":ping_pong::white_small_square::black_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
            ":ping_pong::black_small_square::black_small_square::white_small_square::ping_pong:",
            ":ping_pong::black_small_square::white_small_square::black_small_square::ping_pong:",
    };
}
