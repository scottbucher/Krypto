package me.stqlth.krypto.commands.xp.member;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.XpMessages;
import me.stqlth.krypto.xpSystem.XpMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;

import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class XpLeaderboard extends Command {

    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    private XpMethods xpMethods;
    private XpMessages xpMessages;

    public XpLeaderboard(KryptoConfig kryptoConfig, DebugMessages debugMessages, XpMethods xpMethods, XpMessages xpMessages) {
        this.name = "lb";
        this.aliases = new String[]{"leaderboard", "xptop"};
        this.help = "Display the level leaderboard.";
        this.guildOnly = true;
        this.category = new Category("Tools");
        this.arguments = "[size]";

        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
        this.xpMethods = xpMethods;
        this.xpMessages = xpMessages;
    }

    @Override
    protected void execute(CommandEvent event) {
        int size = 10;
        ArrayList<Member> members = new ArrayList<>();

        String[] args = event.getMessage().getContentRaw().split(" ");

        List<Member> check = event.getGuild().getMembers().stream().filter(member -> !member.getUser().isBot()).collect(Collectors.toList());






        try {
            if (args.length > 1) {
                size = Integer.parseInt(args[1]);
                if (size > 25) size = 25;
                if (size > check.size()) size = check.size();
                members = getMembers(event, size);
            }
        } catch (NumberFormatException e) {
            xpMessages.sendLeaderboardErrorMessage(event.getTextChannel(), event.getMember(), event, "lb");
        }
        if (args.length == 1) {
            if (size > check.size()) size = check.size();
            members = getMembers(event, size);
        }

        DecimalFormat formatter = new DecimalFormat("#,###");

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("__**Leveling Leaderboard**__ (Top " + size + " Members)")
                .setColor(Color.decode("#42f4f4"));

        assert members != null;
        if (size > members.size()) size = members.size();

        for (int i = 0; i < size; i++) {
            Member member = members.get(i);
            int xp = xpMethods.getPlayerXp(member, event);
            int level = xpMethods.getLevelFromXp(xp);

            builder.appendDescription(String.format(
                    "\n`" + (i + 1) + ":` **%s** Player Level: %s\n   (Total XP: %s)\n",
                    member.getEffectiveName(),
                    level,
                    formatter.format(xp)
            ));
        }

        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        builder.setFooter("Be active to advance in the leaderboard!", botIcon);
        event.getTextChannel().sendMessage(builder.build()).queue();

    }

    private ArrayList<Member> getMembers(CommandEvent event, int size) {
        ArrayList<Member> members = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            Guild g = event.getGuild();
            int guildId = xpMethods.getGuildId(event);

            ResultSet rs = statement.executeQuery("SELECT UserDiscordId FROM users JOIN xp ON users.UserId=xp.UserId WHERE " +
                    "(GuildId="+ guildId+ ") ORDER BY XpAmount DESC LIMIT " + size);

            for (int i = 0; i < size; i++) {
                rs.next();
                Member temp = g.getMemberById(rs.getString("UserDiscordId"));
                if (temp == null) return members;

                if (!temp.getUser().isBot()) members.add(temp);
            }
            return members; //if the program starts to throw a NullPointerException error just remove that and change line 114 to return members;
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return null;
    }

}


