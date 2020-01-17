package me.stqlth.krypto.commands.music;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.discordOut.MusicMessages;
import me.stqlth.krypto.music.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Play extends Command {
    private final YouTube youTube;

    private KryptoConfig kryptoConfig;
    private MusicMessages musicMessages;

    public Play(KryptoConfig kryptoConfig, MusicMessages musicMessages)
    {
        this.name = "play";
        this.help = "Play a song.";
        this.guildOnly = true;
        this.category = new Category("Music");
        this.arguments = "<link or song name>";

        this.kryptoConfig = kryptoConfig;
        this.musicMessages = musicMessages;

        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Krypto Bot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;

    }



    @Override
    protected void execute(CommandEvent event) {

        TextChannel channel = event.getTextChannel();
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(args.length <= 1) {
            musicMessages.noArgs(channel, this.help, event);
            return;
        }


        StringBuilder input = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            input.append(args[i]).append(" ");
        }

        if(!isUrl(input.toString())) {

            String ytSearched = searchYoutube(input.toString());

            if (ytSearched == null) {
                 musicMessages.noYTResults(channel, event);
                 return;
            }

            input = new StringBuilder(ytSearched);

        }


        PlayerManager manager = PlayerManager.getInstance(musicMessages);

        manager.loadAndPlay(event.getTextChannel(), input.toString(), event);

        manager.getGuildMusicManager(event.getGuild()).player.setVolume(10);


    }

    private boolean isUrl (String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(kryptoConfig.getYoutubeApiKey())
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
