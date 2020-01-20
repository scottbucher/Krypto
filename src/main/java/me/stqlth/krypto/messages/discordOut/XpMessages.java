package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import me.stqlth.krypto.xpSystem.XpMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class XpMessages {
    private XpMethods xpMethods;
    private GetMessageInfo getMessageInfo;

    public XpMessages(XpMethods xpMethods, GetMessageInfo getMessageInfo) {
        this.xpMethods = xpMethods;
        this.getMessageInfo = getMessageInfo;
    }

    public void xpMessage(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        double playerLevel = xpMethods.getLevelFromXp(event);
        double playerLevelXp = xpMethods.getLevelXp(event);
        double playerXp = xpMethods.getPlayerXp(event.getMember(), event);
        double playerXpTowardsNextLevel = xpMethods.getRemainingXp(event);

        String comp = "\uD83D\uDFE9";
        String non = "\u25AB";
        int totalBars = 10;
        double progressPercent = Math.floor((playerXpTowardsNextLevel / playerLevelXp) * 100);
        float progress = Math.round(((float) playerXpTowardsNextLevel / (float) playerLevelXp) * totalBars);
        StringBuilder progressBar = new StringBuilder();

        int left = totalBars - (int) progress;

        for(int i = 0; i < progress; i++)
            progressBar.append(comp);
        for(int c = 0; c < left; c++)
            progressBar.append(non);
        progressBar.append(" ").append((int) progressPercent).append("%");

        DecimalFormat formatter = new DecimalFormat("#,###");

        builder.setTitle("**" + event.getMember().getEffectiveName() + "'s Leveling Progress**")
                .setColor(Color.decode("#32CD32"))
                .addField("Player Level", "" + playerLevel, false)
                .addField("Player Experience", "" + formatter.format(playerXp), false)
                .addField("Level Progress", formatter.format(playerXpTowardsNextLevel) + " / " + formatter.format(playerLevelXp) + " XP towards level: **" + (playerLevel + 1)
                        + "**\n" + progressBar, false)
                .setFooter("You may only earn xp once per minute, this is to prevent abuse.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void xpMessage(TextChannel channel, Member target, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        int playerLevel = xpMethods.getLevelFromXp(event, target);
        int playerLevelXp = xpMethods.getLevelXp(event, target);
        int playerXpTowardsNextLevel = xpMethods.getRemainingXp(event, target);

        builder.setTitle("**" + target.getEffectiveName() + "'s Leveling Progress**")
                .setColor(Color.decode("#32CD32"))
                .addField("Player Level", "" + playerLevel, false)
                .setDescription(playerXpTowardsNextLevel + "/" + playerLevelXp + " XP towards level: **" + (playerLevel + 1) + "**")
                .setFooter("You may only earn xp once per minute, this is to prevent abuse.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void sendLeaderboardErrorMessage(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + " [Size: Max 25]")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 Skyvade", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void sendSetXpErrorMessage(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + "<@user> <xp>")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 Skyvade", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void sendLevelUpMessage(Member member, int level, GuildMessageReceivedEvent event) {
        String channelId = getMessageInfo.getLevelingChannelId(event);
        if (channelId.equals("0")) return;
        if (event.getGuild().getTextChannelById(channelId) == null) return; //Levelup message if there is no level reward for the specified level.

        Objects.requireNonNull(event.getGuild().getTextChannelById(channelId))
                .sendMessage("**Congratulations " + member.getAsMention() + "** you've reached level __**" + level + "**__").queue();
    }

    public void sendLevelUpMessageWithReward(Member member, int level, GuildMessageReceivedEvent event, List<Role> roles) {
        String channelId = getMessageInfo.getLevelingChannelId(event);
        if (channelId.equals("0")) return;
        if (event.getGuild().getTextChannelById(channelId) == null) return;

        StringBuilder message = new StringBuilder("**Congratulations " + member.getAsMention() + "** you've reached level __**" + level + "**__ and have unlocked the following role(s): ");

        int size = roles.size();

        if (size == 1) {
            message.append(roles.get(0).getName()).append("!"); //Formatting: if there is only one role to add to the user then a compound input isn't required
        } else {
            for (int i = 0; i < size-1; i++)
                message.append(roles.get(i).getName()).append(", "); //if there is more than one then it adds all but the last

            message.append("& ").append(roles.get(size - 1).getName()).append("!"); //adds the last and ends it with proper punctuation
        }
        Objects.requireNonNull(event.getGuild().getTextChannelById(channelId)) //send message
                .sendMessage(message.toString()).queue();
    }

    public void sendLevelUpMessage(Member member, int level, Guild guild) {
        String channelId = getMessageInfo.getLevelingChannelId(guild);
        if (channelId.equals("0")) return;
        if (guild.getTextChannelById(channelId) == null) return; //Levelup message if there is no level reward for the specified level.

        Objects.requireNonNull(guild.getTextChannelById(channelId))
                .sendMessage("**Congratulations " + member.getAsMention() + "** you've reached level __**" + level + "**__").queue();
    }

    public void sendLevelUpMessageWithReward(Member member, int level, Guild guild, List<Role> roles) {
        String channelId = getMessageInfo.getLevelingChannelId(guild);
        if (channelId.equals("0")) return;
        if (guild.getTextChannelById(channelId) == null) return;

        StringBuilder message = new StringBuilder("**Congratulations " + member.getAsMention() + "** you've reached level __**" + level + "**__ and have unlocked the following role(s): ");

        int size = roles.size();

        if (size == 1) {
            message.append(roles.get(0).getName()).append("!"); //Formatting: if there is only one role to add to the user then a compound input isn't required
        } else {
            for (int i = 0; i < size-1; i++)
                message.append(roles.get(i).getName()).append(", "); //if there is more than one then it adds all but the last

            message.append("& ").append(roles.get(size - 1).getName()).append("!"); //adds the last and ends it with proper punctuation
        }
        Objects.requireNonNull(guild.getTextChannelById(channelId)) //send message
                .sendMessage(message.toString()).queue();
    }

    public void setPlayerXp(TextChannel channel, Member target, int xp) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully set " + target.getUser().getName() + "'s XP to " + xp);
        channel.sendMessage(builder.build()).queue();
    }

    public void setLevelingReward(TextChannel channel, Role reward, int level) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully set the role " + reward.getName() + " as a reward for level " + level);
        channel.sendMessage(builder.build()).queue();
    }

    public void removeLevelingReward(TextChannel channel, int level) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully removed the role reward(s) from the level " + level);
        channel.sendMessage(builder.build()).queue();
    }

    public void onlyOwner(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Only Stqlth may use this command!");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }
}
