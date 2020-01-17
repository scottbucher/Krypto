package me.stqlth.krypto.commands.roleCall;


import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.discordOut.VerifyMessages;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetupRoleCall extends Command {
    private VerifyMessages verifyMessages;

    public SetupRoleCall(VerifyMessages verifyMessages) {
        this.name = "setuprolecall";
        this.help = "Setup the role call embed.";
        this.guildOnly = true;
        this.hidden = true;
        this.verifyMessages = verifyMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getMessage().delete().queue();
            return;
        }


        TextChannel rolecall = event.getGuild().getTextChannels().stream().filter(channel -> channel.getName().contains("role-call")).findFirst().orElse(null);
        if (rolecall != null) {
            event.getMessage().delete().queue();
            verifyMessages.roleVerifySetupMessage(event, rolecall);
        }

    }



}
