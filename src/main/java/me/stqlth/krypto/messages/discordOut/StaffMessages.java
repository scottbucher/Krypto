package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StaffMessages {
    private GetMessageInfo getMessageInfo;

    public StaffMessages(GetMessageInfo getMessageInfo) {
        this.getMessageInfo = getMessageInfo;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Punishment Command Messages
    public void sendErrorMessage(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + " <@user> <reason>")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 KryptoBot", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void sendLevelRewardFormatError(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + " <level> <role>")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 KryptoBot", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void sendRemoveLevelingRewardsFormatError(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + " <level>")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 KryptoBot", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void sendErrorMessagePrefix(TextChannel channel, Member member, CommandEvent event, String command) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        Guild g = event.getGuild();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Invalid Usage!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("Proper usage: " + getMessageInfo.getPrefix(g) + command + " <prefix>")
                .appendDescription("\n<> = Required, [] = Optional")
                .setFooter("© 2020 KryptoBot", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void noPerm(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Only Staff may use this command!");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }
    public void prefixTooLarge(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Your prefix can only be 100 characters long.");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }
    public void onlyAdmins(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("Only Admins may use this command!");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void noPunish(TextChannel channel, String command) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("You can't " + command + " this player!");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void notBanned(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("This player is not banned!");
        channel.sendMessage(builder.build()).queue();
    }

    public void roleNotFound(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("The specified role cannot be found!");
        channel.sendMessage(builder.build()).queue();
    }

    public void emoteNotFound(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("The specified emote cannot be found!");
        channel.sendMessage(builder.build()).queue();
    }

    public void noMutedRole(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#EA2027"))
                .setDescription("There is no muted role!");
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }

    public void setPrefix(TextChannel channel, String prefix) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully changed the bot prefix to `" + prefix + "`!");
        channel.sendMessage(builder.build()).queue();
    }

    public void setLevelingChannel(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully changed #" + channel.getName() + " to the level-up notification channel.");
        channel.sendMessage(builder.build()).queue();
    }

    public void disableLevelingChannel(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode("#34f922"))
                .setDescription("Successfully disabled leveling messages.");
        channel.sendMessage(builder.build()).queue();
    }

    public void kickLog(Member target, Member sender, String reason, TextChannel commandChannel, TextChannel logChannel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User Kick")
                .setColor(Color.decode("#f9a11d"))
                .addField("Kicked User", target.getAsMention() + " with ID: " + target.getUser().getId(), false)
                .addField("Kicked By", sender.getAsMention() + " with ID: " + sender.getUser().getId(), false)
                .addField("Reason", reason, false)
                .addField("Kicked in ", commandChannel.getAsMention(), false)
                .addField("Date & Time of Kick", sdf.format(date) + " " + stf.format(date) + " (Eastern Standard Tme)", false);
        logChannel.sendMessage(builder.build()).queue();
    }

    public void muteLog(Member target, Member sender, String reason, TextChannel commandChannel, TextChannel logChannel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User Mute")
                .setColor(Color.decode("#f9a11d"))
                .addField("Muted User", target.getAsMention() + " with ID: " + target.getUser().getId(), false)
                .addField("Muted By", sender.getAsMention() + " with ID: " + sender.getUser().getId(), false)
                .addField("Reason", reason, false)
                .addField("Muted in ", commandChannel.getAsMention(), false)
                .addField("Date & Time of Mute", sdf.format(date) + " " + stf.format(date) + " (Eastern Standard Tme)", false);
        logChannel.sendMessage(builder.build()).queue();
    }

    public void unMuteLog(Member target, Member sender, String reason, TextChannel commandChannel, TextChannel logChannel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User UnMute")
                .setColor(Color.decode("#34f922"))
                .addField("UnMuted User", target.getAsMention() + " with ID: " + target.getUser().getId(), false)
                .addField("UnMuted By", sender.getAsMention() + " with ID: " + sender.getUser().getId(), false)
                .addField("Reason", reason, false)
                .addField("UnMuted in ", commandChannel.getAsMention(), false)
                .addField("Date & Time of UnMute", sdf.format(date) + " " + stf.format(date) + " (Eastern Standard Tme)", false);
        logChannel.sendMessage(builder.build()).queue();
    }

    public void banLog(Member target, Member sender, String reason, TextChannel commandChannel, TextChannel logChannel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User Ban")
                .setColor(Color.decode("#EA2027"))
                .addField("Banned User", target.getAsMention() + " with ID: " + target.getUser().getId(), false)
                .addField("Banned By", sender.getAsMention() + " with ID: " + sender.getUser().getId(), false)
                .addField("Reason", reason, false)
                .addField("Banned in ", commandChannel.getAsMention(), false)
                .addField("Date & Time of Ban", sdf.format(date) + " " + stf.format(date) + " (Eastern Standard Tme)", false);
        logChannel.sendMessage(builder.build()).queue();
    }

    public void unBanLog(User target, Member sender, String reason, TextChannel commandChannel, TextChannel logChannel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("User UnBan")
                .setColor(Color.decode("#34f922"))
                .addField("UnBanned User", target.getAsMention() + " with ID: " + target.getId(), false)
                .addField("UnBanned By", sender.getAsMention() + " with ID: " + sender.getUser().getId(), false)
                .addField("Reason", reason, false)
                .addField("UnBanned in ", commandChannel.getAsMention(), false)
                .addField("Date & Time of UnBan", sdf.format(date) + " " + stf.format(date) + " (Eastern Standard Tme)", false);
        logChannel.sendMessage(builder.build()).queue();
    }

    public void multipleBan(TextChannel channel, Member member, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Multiple Users Found!")
                .setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                .setColor(Color.decode("#EA2027"))
                .setDescription("There are multiple banned user with this name.")
                .appendDescription("\nBecause of this, and to avoid any unintentional" + " \nunbans. You must manually"
                        + " unban this player")
                .setFooter("© 2020 KryptoBot", botIcon);
        channel.sendMessage(builder.build()).complete().delete().queueAfter(15, TimeUnit.SECONDS);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
