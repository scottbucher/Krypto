package me.stqlth.krypto.messages.discordOut;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import me.stqlth.krypto.music.FormatTime;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class MusicMessages {
    
    private GetMessageInfo getMessageInfo;
    public MusicMessages(GetMessageInfo getMessageInfo) {
        this.getMessageInfo = getMessageInfo;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //PlayerManager.java MusicMessages
    public void addSingleToQueue(TextChannel channel, AudioTrack track, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        FormatTime ft = new FormatTime();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Added...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("Adding [" + track.getInfo().title + "](" + track.getInfo().uri + ") to your music queue.")
                .setFooter("Track Length: `" + ft.formatTime(track.getDuration()) + "`", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void addPlayListToQueue(TextChannel channel, AudioTrack firstTrack, AudioPlaylist playlist, CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();

        builder.setTitle("**Adding...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("Added " + firstTrack.getInfo().title + " to your music queue (first track of playlist " + playlist.getName() + ")")
                .setFooter("Enqueued `" + playlist.getTracks().size() + "` songs",botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Play.java MusicMessages
    public void noMatch(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("No matches were found.")
                .setFooter("Ensure you have the correct link or try a different source.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void loadFailed(TextChannel channel, FriendlyException exception, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Could not play " + exception.getMessage())
                .setFooter("Load Failed", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void noArgs(TextChannel channel, String help, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription(help)
                .setFooter("Please provide a link.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void invalidLink(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Please provide a valid youtube, soundcloud, or bandcamp link")
                .setFooter("Link is invalid.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void noYTResults(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Youtube returned no results")
                .setFooter("Youtube Search is invalid", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //StopMusic.java MusicMessages
    public void stopCommandSuccess(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Stopping...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The music has been stopped")
                .setFooter("To listen to music again use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void botNotConnected(TextChannel channel, CommandEvent event) { //USED IN MULTIPLE CLASSES AS IT IS A GENERAL CHECK
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("The music bot is not connected to a channel")
                .setFooter("To bring the music bot into your channel use the `" + getMessageInfo.getPrefix(event.getGuild()) + "summon` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void userNotConnected(TextChannel channel, CommandEvent event) { //USED IN MULTIPLE CLASSES AS IT IS A GENERAL CHECK
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("You are not connected to a channel")
                .setFooter("Join a voice channel to use this command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Queue.java MusicMessages
    public void queueIsEmpty(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("The music queue is currently empty.")
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Skip.java MusicMessages
    public void noCurrentSong(TextChannel channel, CommandEvent event) {//THIS MESSAGE IS ALSO USED IN NowPlaying.java AS THEY BOTH CHECK TO EITHER ACT UPON OR GET INFO FROM THE CURRENT PLAYING SONG
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("There is no song currently playing!")
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void skippingTrack(TextChannel channel, CommandEvent event, AudioTrackInfo currentTrack, AudioTrackInfo nowPlaying) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Skipping...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("Skipping [" + currentTrack.title + "](" + currentTrack.uri + ")\nNow Playing: [" +
                        nowPlaying.title + "](" + nowPlaying.uri + ")")
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //NowPlaying.java
    public void nowPlayingSong(TextChannel channel, CommandEvent event, AudioTrackInfo currentTrack, AudioTrackInfo nextPlaying, AudioPlayer player) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        FormatTime ft = new FormatTime();
        EmbedBuilder builder = new EmbedBuilder();


        builder.setTitle("**Now Playing**")
                .setColor(Color.decode("#32CD32"))
                .setDescription(String.format("%s [%s](%s) `%s - %s`\n",
                        player.isPaused() ? "\u23F8" : "\u25B6",
                        currentTrack.title,
                        currentTrack.uri,
                        ft.formatTime(player.getPlayingTrack().getPosition()),
                        ft.formatTime(player.getPlayingTrack().getDuration())))
                .addField("**Next Up**", String.format("[%s](%s) `%s`\n",
                        nextPlaying.title,
                        nextPlaying.uri,
                        ft.formatTime(nextPlaying.length)), false)
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void nowPlayingSong(TextChannel channel, CommandEvent event, AudioTrackInfo currentTrack, AudioPlayer player) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        FormatTime ft = new FormatTime();
        EmbedBuilder builder = new EmbedBuilder();


        builder.setTitle("**Now Playing**")
                .setColor(Color.decode("#32CD32"))
                .setDescription(String.format("%s [%s](%s) `%s - %s`\n",
                        player.isPaused() ? "\u23F8" : "\u25B6",
                        currentTrack.title,
                        currentTrack.uri,
                        ft.formatTime(player.getPlayingTrack().getPosition()),
                        ft.formatTime(player.getPlayingTrack().getDuration())))
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //PausedCommand.java MusicMessages
    public void pausedSong(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Pausing...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The music has been paused")
                .setFooter("To resume your music, please use the `" + getMessageInfo.getPrefix(event.getGuild()) + "resume` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Resume.java MusicMessages
    public void resumeSong(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Resuming...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The music has been resumed")
                .setFooter("To pause your music, please use the `" + getMessageInfo.getPrefix(event.getGuild()) + "pause` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void notPaused(TextChannel channel, CommandEvent event) {//THIS MESSAGE IS ALSO USED IN NowPlaying.java AS THEY BOTH CHECK TO EITHER ACT UPON OR GET INFO FROM THE CURRENT PLAYING SONG
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("There is no song currently paused!")
                .setFooter("To pause your music, please use the `" + getMessageInfo.getPrefix(event.getGuild()) + "pause` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Clear.java MusicMessages
    public void clearQueue(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Clearing...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The Queue has been cleared!")
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void emptyQueue(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("There is nothing in the queue!")
                .setFooter("To add to your music queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "play` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Move.java MusicMessages
    public void movedSong(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Moving...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The song has been moved")
                .setFooter("To view the music in your queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "queue` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void notEnoughMoveArgs(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Incorrect usage")
                .setFooter("To use this command type `" + getMessageInfo.getPrefix(event.getGuild()) + "move <song number in " + getMessageInfo.getPrefix(event.getGuild()) + "queue" + "> <position number>", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void invalidLocation(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Cannot Access Location**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Invalid Location")
                .setFooter("One ore more of your given locations is invalid, please check `" + getMessageInfo.getPrefix(event.getGuild()) + "queue` for location values.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //SkipTo.java MusicMessages
    public void notEnoughSkipToArgs(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Incorrect usage")
                .setFooter("To use this command type `" + getMessageInfo.getPrefix(event.getGuild()) + "skipto <song number>", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void skippedTo(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Skipping...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The selected song has been skipped to.")
                .setFooter("To view the music in your queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "queue` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Replay.java MusicMessages
    public void replaySong(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Replaying...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The selected song is now being replayed")
                .setFooter("To view the music currently playing use the `" + getMessageInfo.getPrefix(event.getGuild()) + "np` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Remove.java MusicMessages
    public void notEnoughRemoveArgs(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Error!**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Incorrect usage")
                .setFooter("To use this command type `" + getMessageInfo.getPrefix(event.getGuild()) + "remove <song number>", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void invalidOneLocation(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Cannot Access Location**")
                .setColor(Color.decode("#EA2027"))
                .setDescription("Invalid Location")
                .setFooter("The song location your specified is invalid. Use `" + getMessageInfo.getPrefix(event.getGuild()) + "queue` for location values.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }

    public void removedSong(TextChannel channel, CommandEvent event) {
        SelfUser bot = event.getJDA().getSelfUser();
        String botIcon = bot.getAvatarUrl();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("**Removing...**")
                .setColor(Color.decode("#32CD32"))
                .setDescription("The selected song has been removed.")
                .setFooter("To view the music in your queue use the `" + getMessageInfo.getPrefix(event.getGuild()) + "queue` command.", botIcon);
        channel.sendMessage(builder.build()).queue();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
