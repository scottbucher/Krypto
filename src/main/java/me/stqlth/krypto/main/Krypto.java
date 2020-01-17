package me.stqlth.krypto.main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.stqlth.krypto.commands.music.*;
import me.stqlth.krypto.commands.owner.SetXp;
import me.stqlth.krypto.commands.roleCall.SetupRoleCall;
import me.stqlth.krypto.commands.staff.*;
import me.stqlth.krypto.commands.user.*;
import me.stqlth.krypto.commands.xp.member.Level;
import me.stqlth.krypto.commands.xp.member.XpLeaderboard;
import me.stqlth.krypto.commands.xp.staff.AddLevelingReward;
import me.stqlth.krypto.commands.xp.staff.DisabledLevelingMessages;
import me.stqlth.krypto.commands.xp.staff.RemoveLevelingReward;
import me.stqlth.krypto.commands.xp.staff.SetLevelingChannel;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.events.GuildJoinLeave;
import me.stqlth.krypto.events.GuildMemberJoinLeave;
import me.stqlth.krypto.events.GuildMessageReceived;
import me.stqlth.krypto.main.GuildSettings.SettingsManager;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.*;
import me.stqlth.krypto.messages.getMethods.GetMessageInfo;
import me.stqlth.krypto.utils.Logger;
import me.stqlth.krypto.verifyRoles.VerifyRolesEvents;
import me.stqlth.krypto.xpSystem.XpLevelUp;
import me.stqlth.krypto.xpSystem.XpMethods;
import me.stqlth.krypto.xpSystem.XpSystem;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;

public class Krypto {
    private static final String CONFIG_FILE = "botconfig.json";
    private static final String SUCCESS_EMOJI = "\uD83D\uDE03";
    private static final String WARNING_EMOJI = "\uD83D\uDE2E";
    private static final String ERROR_EMOJI = "\uD83D\uDE26";

    public static void main(String[] args) {
        try {
            startBot();
        } catch (IllegalArgumentException e) {
            Logger.Warn("No login details provided! Please provide a botToken in the config file.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startBot() throws IOException, LoginException {
        Logger.Info("Application started.");

        // Load config
        Logger.Info("Loading config...");
        KryptoConfig kryptoConfig = loadConfig();

        // Construct dependencies
        DebugMessages debugMessages = new DebugMessages();
        VerifyMessages verifyMessages = new VerifyMessages();
        EventWaiter waiter = new EventWaiter();
        XpMethods xpMethods = new XpMethods(kryptoConfig, debugMessages);
        AnnounceMessages announceMessages = new AnnounceMessages();
        PollMessages pollMessages = new PollMessages();
        QuoteMessages quoteMessages = new QuoteMessages();
        GetMessageInfo getMessageInfo = new GetMessageInfo(kryptoConfig, debugMessages);
        MusicMessages musicMessages = new MusicMessages(getMessageInfo);
        JoinLeaveMessages joinLeaveMessages = new JoinLeaveMessages();
        StaffMessages staffMessages = new StaffMessages(getMessageInfo);
        XpMessages xpMessages = new XpMessages(xpMethods, getMessageInfo);
        XpLevelUp xpLevelUp = new XpLevelUp(kryptoConfig, debugMessages, xpMessages);
        EightBallMessages eightBallMessages = new EightBallMessages();
        XpSystem xpSystem = new XpSystem(kryptoConfig, xpMethods, debugMessages, xpLevelUp, waiter);

        SettingsManager settingsManager = new SettingsManager(kryptoConfig, debugMessages);

        Command[] commands = new Command[]{
                // Info
                new Hello(waiter),
                new About(kryptoConfig, getMessageInfo),
                new ServerInfo(getMessageInfo),
                new WhatShard(),

                // Tools
                new Level(xpMessages),
                new XpLeaderboard(kryptoConfig, debugMessages, xpMethods, xpMessages),
                new Poll(pollMessages),
                new Ping(),

                // Fun
                new Quote(quoteMessages),
                new EightBall(eightBallMessages),

                // Music - Controls
                new Summon(getMessageInfo, musicMessages),
                new Play(kryptoConfig, musicMessages),
                new StopMusic(musicMessages),
                new Pause(musicMessages),
                new Resume(musicMessages),
                new Replay(musicMessages),
                new NowPlaying(musicMessages),
                new Leave(getMessageInfo, musicMessages),

                // Music - Queue
                new Queue(getMessageInfo, musicMessages),
                new Move(musicMessages),
                new Skip(musicMessages),
                new SkipTo(musicMessages),
                new Remove(musicMessages),
                new Clear(musicMessages),

                // Admin Commands
                new Announce(announceMessages),
                new Kick(staffMessages),
                new Ban(staffMessages),
                new UnBan(staffMessages),
                new Mute(staffMessages),
                new UnMute(staffMessages),
                new SetPrefix(kryptoConfig, staffMessages, debugMessages),
                new SetLevelingChannel(kryptoConfig, staffMessages, debugMessages),
                new DisabledLevelingMessages(kryptoConfig, debugMessages, staffMessages),
                new SetXp(kryptoConfig, debugMessages, xpMethods, xpMessages),
                new AddLevelingReward(kryptoConfig, debugMessages, xpMethods, xpMessages, staffMessages),
                new RemoveLevelingReward(kryptoConfig, debugMessages, xpMethods, xpMessages, staffMessages),
                new SetupRoleCall(verifyMessages)
        };

        // Create the client
        CommandClient client = createClient(kryptoConfig, settingsManager, commands);

        EventListener[] listeners = new EventListener[]{
                waiter,
                new JoinLeaveMessages(),
                new GuildJoinLeave(kryptoConfig, debugMessages),
                new VerifyRolesEvents(),
                new GuildMemberJoinLeave(kryptoConfig, debugMessages, joinLeaveMessages),
                new XpSystem(kryptoConfig, xpMethods, debugMessages, xpLevelUp, waiter),
                new GuildMessageReceived()
        };

        // Start the shard manager
        ShardManager instance = null;

        Logger.Info("Starting shard manager...");
        try {
            instance = startShardManager(kryptoConfig, client, listeners);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SetupGuilds(kryptoConfig, instance.getGuilds(), debugMessages);
            xpSystem.StartVoiceXP(instance); //begins endless loop, do not do anything after this
        } catch (LoginException ex) {
            Logger.Error("Error encountered while logging in. The bot token may be incorrect.", ex);
        }
    }

    private static KryptoConfig loadConfig() throws IOException {
        Path configFilePath = new File(CONFIG_FILE).toPath();
        String configData = new String(Files.readAllBytes(configFilePath));
        JSONObject configJson = new JSONObject(configData);
        return new KryptoConfig(configJson);
    }

    private static CommandClient createClient(KryptoConfig kryptoConfig, SettingsManager settingsManager, Command[] commands) {
        CommandClientBuilder clientBuilder = new CommandClientBuilder();
        clientBuilder.setGuildSettingsManager(settingsManager)
                .useDefaultGame()
                .setOwnerId(kryptoConfig.getOwnerId())
                .setActivity(Activity.watching("you"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setEmojis(SUCCESS_EMOJI, WARNING_EMOJI, ERROR_EMOJI)
                .addCommands(commands);
        return clientBuilder.build();
    }

    private static ShardManager startShardManager(KryptoConfig kryptoConfig, CommandClient client, EventListener[] listeners) throws LoginException {
        DefaultShardManagerBuilder shardManagerBuilder = new DefaultShardManagerBuilder();
        return shardManagerBuilder.setToken(kryptoConfig.getToken())
                .addEventListeners((Object[]) listeners)
                .addEventListeners(client)
                .build();
    }

    private static void SetupGuilds(KryptoConfig kryptoConfig,  List<Guild> guildList, DebugMessages debugMessages) {
        for (Guild check : guildList) {
            if (!guildExists(kryptoConfig, debugMessages, check))
                AddGuildToDatabase(kryptoConfig, check, debugMessages);
        }
    }

    private static boolean guildExists(KryptoConfig kryptoConfig, DebugMessages debugMessages, Guild g) {
        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet check = statement.executeQuery("SELECT COUNT(*) > 0 AS AlreadyExists FROM guild WHERE DiscordId='" + g.getId() + "'");
            check.next();
            boolean alreadyExists = check.getBoolean("AlreadyExists");

            if (alreadyExists) return true;

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
        return false;
    }

    private static void AddGuildToDatabase(KryptoConfig kryptoConfig, Guild g, DebugMessages debugMessages) {

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

                statement.execute("INSERT INTO guildsettings (Prefix) VALUES('!')");
                ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
                rs.next();
                int lastId = rs.getInt("LAST_INSERT_ID()");
                statement.execute("INSERT INTO guild (DiscordId, GuildSettingsId, Active) " + "VALUES('" + g.getId() + "', '" + lastId + "', '1')");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        for (Member mem : g.getMembers()) {
            addUser(kryptoConfig, g, mem, debugMessages);
        }

    }

    public static void addUser(KryptoConfig kryptoConfig, Guild g, Member member, DebugMessages debugMessages) {

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet guildId = statement.executeQuery("SELECT GuildId FROM guild WHERE DiscordId='" + g.getId() + "'");
            guildId.next();
            int discordId = guildId.getInt("GuildId");

            ResultSet check = statement.executeQuery("SELECT COUNT(*) > 0 AS UserAlreadyExists FROM users WHERE UserDiscordId='" + member.getId() + "'");
            check.next();
            boolean UserAlreadyExists = check.getBoolean("UserAlreadyExists");


            if (!UserAlreadyExists) {
                statement.execute("INSERT INTO users (UserDiscordId) VALUES('" + member.getId() + "')");
            }


            ResultSet UserId = statement.executeQuery("SELECT UserId FROM users WHERE UserDiscordId='" + member.getId() + "'");
            UserId.next();
            int userId = UserId.getInt("UserId");

            ResultSet check2 = statement.executeQuery("SELECT COUNT(*) > 0 AS DiscordAndUserExistsInXp FROM xp WHERE GuildId='" + discordId + "' AND UserId='" + userId + "'");
            check2.next();
            boolean DiscordAndUserExistsInXp = check2.getBoolean("DiscordAndUserExistsInXp");


            if (!DiscordAndUserExistsInXp) {
                statement.execute("INSERT INTO xp (GuildId, UserId) " + "VALUES('" + discordId + "', '" + userId + "')");
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }
}
