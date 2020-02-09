package me.stqlth.krypto.commands.xp.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.StaffMessages;
import me.stqlth.krypto.messages.discordOut.XpMessages;
import me.stqlth.krypto.xpSystem.XpMethods;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class AddLevelingReward extends Command {

    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    private XpMethods xpMethods;
    private XpMessages xpMessages;
    private StaffMessages staffMessages;

    public AddLevelingReward(KryptoConfig kryptoConfig, DebugMessages debugMessages, XpMethods xpMethods, XpMessages xpMessages, StaffMessages staffMessages) {
        this.name = "addlevelingreward";
        this.help = "Add a role reward for reaching a level.";
        this.guildOnly = true;
        this.hidden = true;

        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
        this.xpMethods = xpMethods;
        this.xpMessages = xpMessages;
        this.staffMessages = staffMessages;
    }

    @Override
    protected void execute(CommandEvent event) {

        Permission req = Permission.ADMINISTRATOR;
        Member member = event.getMember();
        TextChannel channel = event.getTextChannel();

        if (!member.hasPermission(req)) {
            staffMessages.onlyAdmins(channel); //Only admins may run this command
            return;
        }

        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 3) { //Need atleast 3 args to run this command, default command level, and role
            staffMessages.sendLevelRewardFormatError(channel, member, event, getName());
            return;
        }

        int level;

        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            staffMessages.sendLevelRewardFormatError(channel, member, event, getName());
            return;
        }

        Role reward = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains(args[2])).findFirst().orElse(null);
        if (reward == null) {
            staffMessages.roleNotFound(channel); //if it can't find the role tell the user then return;
            return;
        }

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
            Statement statement = conn.createStatement()) {

            int guildId = xpMethods.getGuildId(event);
            statement.execute("CALL InsertReward(" + guildId + ", " + level + ", '" + reward.getName() + "')");
            xpMessages.setLevelingReward(channel, reward, level);
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
            return;
        }

    }
}
