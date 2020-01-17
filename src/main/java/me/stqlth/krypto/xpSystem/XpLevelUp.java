package me.stqlth.krypto.xpSystem;

import me.stqlth.krypto.config.KryptoConfig;
import me.stqlth.krypto.messages.debug.DebugMessages;
import me.stqlth.krypto.messages.discordOut.XpMessages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XpLevelUp {
    private KryptoConfig kryptoConfig;
    private DebugMessages debugMessages;
    private XpMessages xpMessages;

    public XpLevelUp(KryptoConfig kryptoConfig, DebugMessages debugMessages, XpMessages xpMessages) {
        this.kryptoConfig = kryptoConfig;
        this.debugMessages = debugMessages;
        this.xpMessages = xpMessages;
    }

    public void onPlayerLevelUp(GuildMessageReceivedEvent event, int newLevel) {

        List<Integer> rewardLevels = new ArrayList<>();
        List<String> rewardRoles = new ArrayList<>();
        List<Role> rewards = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            int guildId = getGuildId(event);

            ResultSet number = statement.executeQuery("SELECT COUNT(Level) as Amount FROM rewards WHERE GuildId='" + guildId + "'");
            number.next();
            int numberOfLevels = number.getInt("Amount");

            ResultSet rs = statement.executeQuery("SELECT Level FROM rewards WHERE GuildId='" + guildId + "'");

            for (int i = 0; i < numberOfLevels; i++) {
                rs.next();
                rewardLevels.add(rs.getInt("Level"));
            }

            ResultSet roles;
            boolean isLevelUpLevel = false;
            int levelRoleAmount = 0;

            for (int c = 0; c < numberOfLevels; c++) {
                if (newLevel == rewardLevels.get(c)) {

                    isLevelUpLevel = true;
                    ResultSet levelNum = statement.executeQuery("SELECT COUNT(Level) as Amount FROM rewards WHERE GuildId='" + guildId + "' AND Level='" + rewardLevels.get(c) + "'");
                    levelNum.next();
                    levelRoleAmount = levelNum.getInt("Amount");
                    roles = statement.executeQuery("SELECT Role FROM rewards WHERE GuildId='" + guildId + "' AND Level='" + rewardLevels.get(c) + "'");

                    for (int i  = 0; i < levelRoleAmount; i++) {
                        roles.next();
                        rewardRoles.add(roles.getString("Role"));
                    }

                }
            }

            for (int i = 0; i < levelRoleAmount; i++) {
                int counter = 0;
                int finalI = i;
                Role reward = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains(rewardRoles.get(finalI).toLowerCase())).findFirst().orElse(null);
                if (reward == null) {
                    rewardRoles.remove(i);
                    rewardLevels.remove(i);
                    counter++;
                } else rewards.add(reward);
                levelRoleAmount -= counter;
            }


            if (!isLevelUpLevel || rewards.isEmpty()){
                xpMessages.sendLevelUpMessage(event.getMember(), newLevel, event);
                return;
            }

            Member target = event.getMember();
            for (int i = 0; i < levelRoleAmount; i++) {
                assert target != null;
                event.getGuild().addRoleToMember(target, rewards.get(i)).queue();
            }

            xpMessages.sendLevelUpMessageWithReward(event.getMember(), newLevel, event, rewards);

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

    }

    public void onPlayerLevelUp(Guild guild, Member target, int newLevel) {

        List<Integer> rewardLevels = new ArrayList<>();
        List<String> rewardRoles = new ArrayList<>();
        List<Role> rewards = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            int guildId = getGuildId(guild);

            ResultSet number = statement.executeQuery("SELECT COUNT(Level) as Amount FROM rewards WHERE GuildId='" + guildId + "'");
            number.next();
            int numberOfLevels = number.getInt("Amount");

            ResultSet rs = statement.executeQuery("SELECT Level FROM rewards WHERE GuildId='" + guildId + "'");

            for (int i = 0; i < numberOfLevels; i++) {
                rs.next();
                rewardLevels.add(rs.getInt("Level"));
            }

            ResultSet roles;
            boolean isLevelUpLevel = false;
            int levelRoleAmount = 0;

            for (int c = 0; c < numberOfLevels; c++) {
                if (newLevel == rewardLevels.get(c)) {

                    isLevelUpLevel = true;
                    ResultSet levelNum = statement.executeQuery("SELECT COUNT(Level) as Amount FROM rewards WHERE GuildId='" + guildId + "' AND Level='" + rewardLevels.get(c) + "'");
                    levelNum.next();
                    levelRoleAmount = levelNum.getInt("Amount");
                    roles = statement.executeQuery("SELECT Role FROM rewards WHERE GuildId='" + guildId + "' AND Level='" + rewardLevels.get(c) + "'");

                    for (int i  = 0; i < levelRoleAmount; i++) {
                        roles.next();
                        rewardRoles.add(roles.getString("Role"));
                    }

                }
            }

            for (int i = 0; i < levelRoleAmount; i++) {
                int counter = 0;
                int finalI = i;
                Role reward = guild.getRoles().stream().filter(role -> role.getName().toLowerCase().contains(rewardRoles.get(finalI).toLowerCase())).findFirst().orElse(null);
                if (reward == null) {
                    rewardRoles.remove(i);
                    rewardLevels.remove(i);
                    counter++;
                } else rewards.add(reward);
                levelRoleAmount -= counter;
            }


            if (!isLevelUpLevel || rewards.isEmpty()){
                xpMessages.sendLevelUpMessage(target, newLevel, guild);
                return;
            }

            for (int i = 0; i < levelRoleAmount; i++) {
                assert target != null;
                guild.addRoleToMember(target, rewards.get(i)).queue();
            }

            xpMessages.sendLevelUpMessageWithReward(target, newLevel, guild, rewards);

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

    }

    private int getGuildId(GuildMessageReceivedEvent event) {
        int guildId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {
            Guild g = event.getGuild();

            ResultSet rs = statement.executeQuery("SELECT GuildId FROM guild WHERE DiscordId='" + g.getId() + "'");
            rs.next();
            guildId = rs.getInt("GuildId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return guildId;
    }

    private int getGuildId(Guild guild) {
        int guildId = -1;

        try (Connection conn = DriverManager.getConnection(kryptoConfig.getDbUrl(), kryptoConfig.getDbUser(), kryptoConfig.getDbPassword());
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT GuildId FROM guild WHERE DiscordId='" + guild.getId() + "'");
            rs.next();
            guildId = rs.getInt("GuildId");

        } catch (SQLException ex) {
            debugMessages.sqlDebug(ex);
        }

        return guildId;
    }

}
