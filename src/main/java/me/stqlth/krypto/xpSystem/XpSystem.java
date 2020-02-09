package me.stqlth.krypto.xpSystem;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class XpSystem extends ListenerAdapter {

    private KryptoConfig kryptoConfig;
    private XpMethods xpMethods;
    private DebugMessages debugMessages;
    private XpLevelUp xpLevelUp;
    private EventWaiter waiter;

    public XpSystem(KryptoConfig kryptoConfig, XpMethods xpMethods, DebugMessages debugMessages, XpLevelUp xpLevelUp, EventWaiter waiter) {
        this.kryptoConfig = kryptoConfig;
        this.xpMethods = xpMethods;
        this.debugMessages = debugMessages;
        this.xpLevelUp = xpLevelUp;
        this.waiter = waiter;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Voice Chat based XP
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void StartVoiceXP(ShardManager client) {
        while (true) {
            List<Guild> guildList = client.getGuilds();

            if (guildList.isEmpty()) return;

            for (Guild guild : guildList) {
                List<GuildVoiceState> mVoiceStates = guild.getVoiceStates();

                for (GuildVoiceState vs : mVoiceStates) {
                    User target = vs.getMember().getUser();
                    if (vs.inVoiceChannel()) {
                        if (vs.getChannel() != guild.getAfkChannel() && !target.isBot()) {
                            int currentLevel = xpMethods.getLevelFromXp(getPlayerXp(guild, target));
                            voiceXp(guild, target);
                            int newLevel = xpMethods.getLevelFromXp(getPlayerXp(guild, target));
                            if ((currentLevel != newLevel)) {
                                xpLevelUp.onPlayerLevelUp(guild, vs.getMember(), newLevel);
                            }
                        }
                    }
                }
            }
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void voiceXp(Guild guild, User user) {
        setPlayerVoiceXp(getPlayerVoiceXp(guild, user) + 3, guild, user);
    }

    private void setPlayerVoiceXp(int xp, Guild guild, User user) {

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            statement.execute("CALL UpdateXp(" + xp + ", " + getGuildId(guild) + ", " + getUserId(user) + ")");
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }

    private int getPlayerVoiceXp(Guild guild, User user) {
        int xp = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetXp(" + getGuildId(guild) + ", " + getUserId(user) + ")");
            rs.next();
            xp = rs.getInt("XpAmount");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return xp;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Text based XP
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (canGetXp(event) && !event.getAuthor().isBot()) {
            int currentLevel = xpMethods.getLevelFromXp(getPlayerXp(event));

            randXp(event);
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            setPlayerTime(event);

            int newLevel = xpMethods.getLevelFromXp(getPlayerXp(event));
            if ((currentLevel != newLevel) && (event.getMember() != null)) {

                //xpLevelup instance usage
                xpLevelUp.onPlayerLevelUp(event, newLevel);

            }
        }
    }

    private void setPlayerTime(GuildMessageReceivedEvent event) {
        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            statement.execute("CALL UpdateLastUpdated(" + getGuildId(event) + ", " + getUserId(event) + ")");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }

    private void setPlayerXp(int xp, GuildMessageReceivedEvent event) {
        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            statement.execute("CALL UpdateXp(" + xp + ", " + getGuildId(event) + ", " + getUserId(event) + ")");
        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }
    }

    private void randXp(GuildMessageReceivedEvent event) {
        Random r = new Random();
        setPlayerXp(getPlayerXp(event) + ((r.nextInt(10) + 1) + 15), event);
    }

    private boolean canGetXp(GuildMessageReceivedEvent event) {
        Date date = new Date();
        Timestamp currentTime = new Timestamp(date.getTime());
        Timestamp lastUpdated = getPlayerTime(event);

        long milliseconds = currentTime.getTime() - lastUpdated.getTime();
        int seconds = (int) milliseconds / 1000;

        return seconds >= 60;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Text XP Info from the database
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private int getGuildId(GuildMessageReceivedEvent event) {
        int guildId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            Guild g = event.getGuild();

            ResultSet rs = statement.executeQuery("CALL GetGuildId(" + g.getId() + ")");
            rs.next();
            guildId = rs.getInt("GuildId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return guildId;
    }

    private int getUserId(GuildMessageReceivedEvent event) {
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetUserId(" + Objects.requireNonNull(event.getMember()).getUser().getId() + ")");
            rs.next();
            userId = rs.getInt("UserId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return userId;
    }

    private Timestamp getPlayerTime(GuildMessageReceivedEvent event) {
        Date date = new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetPlayerTime(" + getGuildId(event) + ", " + getUserId(event) + ")");
            rs.next();

            if (rs.getTimestamp("LastUpdated") != null) {
                return rs.getTimestamp("LastUpdated");
            }

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return timestamp;
    }

    private int getPlayerXp(GuildMessageReceivedEvent event) {
        int xp = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetXp(" + getGuildId(event) + ", " + getUserId(event) + ")");
            rs.next();
            xp = rs.getInt("XpAmount");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return xp;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Voice XP Info from the database
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private int getGuildId(Guild guild) {
        int guildId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetGuildId(" + guild.getId() + ")");
            rs.next();
            guildId = rs.getInt("GuildId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return guildId;
    }

    private int getUserId(User user) {
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetUserId(" + user.getId() + ")");
            rs.next();
            userId = rs.getInt("UserId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return userId;
    }

    private int getPlayerXp(Guild guild, User user) {
        int xp = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("CALL GetXp(" + getGuildId(guild) + ", " + getUserId(user) + ")");
            rs.next();
            xp = rs.getInt("XpAmount");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return xp;
    }
}
