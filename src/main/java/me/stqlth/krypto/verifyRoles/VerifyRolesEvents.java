package me.stqlth.krypto.verifyRoles;

import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VerifyRolesEvents extends ListenerAdapter {

    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        if (!event.getUser().isBot()) {
            TextChannel roleVerify = event.getGuild().getTextChannels().stream().filter(textchannel -> textchannel.getName().toLowerCase().contains("role-call")).findFirst().orElse(null);
            if (roleVerify != null && roleVerify == event.getChannel()) addCategoryRoles(event);
        }
    }

    public void onMessageReactionRemove (MessageReactionRemoveEvent event) {

        if (!event.getUser().isBot()) {
            TextChannel roleVerify = event.getGuild().getTextChannels().stream().filter(textchannel -> textchannel.getName().toLowerCase().contains("role-call")).findFirst().orElse(null);
            if (roleVerify != null && roleVerify == event.getChannel()) removeCategoryRoles(event);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    private static void addCategoryRoles (MessageReactionAddEvent event) {
        Member member = event.getMember();
        if (member == null) return;

        Guild guild = event.getGuild();
        List<Role> roles = guild.getRoles();

        switch (event.getReactionEmote().getName()) {
            case "😂":
                Role memesRole = findRole(roles, "memes");
                if (memesRole != null) addRoleToMember(guild, member, memesRole);
                break;
            case "🎮":
                Role gamesRole = findRole(roles, "games");
                if (gamesRole != null) addRoleToMember(guild, member, gamesRole);
                break;
            case "🎵":
                Role musicRole = findRole(roles, "music");
                if (musicRole != null) addRoleToMember(guild, member, musicRole);
                break;
            case "💢":
                Role animeRole = findRole(roles, "anime");
                if (animeRole != null) addRoleToMember(guild, member, animeRole);
                break;
            case "🐱":
                Role animalsRole = findRole(roles, "animals");
                if (animalsRole != null) addRoleToMember(guild, member, animalsRole);
                break;
            case "🛑":
                Role nnsfwRole = findRole(roles, "nnsfw");
                if (nnsfwRole != null) addRoleToMember(guild, member, nnsfwRole);
                break;
            case "🎭":
                Role roleplayRole = findRole(roles, "roleplay");
                if (roleplayRole != null) addRoleToMember(guild, member, roleplayRole);
                break;
            case "⛏":
                Role minecraftRole = findRole(roles, "minecraft");
                if (minecraftRole != null) addRoleToMember(guild, member, minecraftRole);
                break;
            case "🍂":
                Role animalCrossingRole = findRole(roles, "animal crossing");
                if (animalCrossingRole != null) addRoleToMember(guild, member, animalCrossingRole);
                break;
            case "🥚":
                Role pokemonRole = findRole(roles, "pokemon");
                if (pokemonRole != null) addRoleToMember(guild, member, pokemonRole);
                break;
            case "⚔":
                Role runescapeRole = findRole(roles, "runescape");
                if (runescapeRole != null) addRoleToMember(guild, member, runescapeRole);
                break;
            case "🍿":
                Role movieStreamsRole = findRole(roles, "movie streams");
                if (movieStreamsRole != null) addRoleToMember(guild, member, movieStreamsRole);
                break;
            case "👾":
                Role gameStreamsRole = findRole(roles, "game streams");
                if (gameStreamsRole != null) addRoleToMember(guild, member, gameStreamsRole);
                break;
            default:
                event.getReaction().removeReaction().queue();
                break;
        }
    }

    private static void removeCategoryRoles (MessageReactionRemoveEvent event) {
        Member member = event.getMember();
        if (member == null) return;

        Guild guild = event.getGuild();
        List<Role> roles = guild.getRoles();

        switch (event.getReactionEmote().getName()) {
            case "😂":
                Role memesRole = findRole(roles, "memes");
                if (memesRole != null) removeRoleFromMember(guild, member, memesRole);
                break;
            case "🎮":
                Role gamesRole = findRole(roles, "games");
                if (gamesRole != null) removeRoleFromMember(guild, member, gamesRole);
                break;
            case "🎵":
                Role musicRole = findRole(roles, "music");
                if (musicRole != null) removeRoleFromMember(guild, member, musicRole);
                break;
            case "💢":
                Role animeRole = findRole(roles, "anime");
                if (animeRole != null) removeRoleFromMember(guild, member, animeRole);
                break;
            case "🐱":
                Role animalsRole = findRole(roles, "animals");
                if (animalsRole != null) removeRoleFromMember(guild, member, animalsRole);
                break;
            case "🛑":
                Role nnsfwRole = findRole(roles, "nnsfw");
                if (nnsfwRole != null) removeRoleFromMember(guild, member, nnsfwRole);
                break;
            case "🎭":
                Role roleplayRole = findRole(roles, "roleplay");
                if (roleplayRole != null) removeRoleFromMember(guild, member, roleplayRole);
                break;
            case "⛏":
                Role minecraftRole = findRole(roles, "minecraft");
                if (minecraftRole != null) removeRoleFromMember(guild, member, minecraftRole);
                break;
            case "🍂":
                Role animalCrossingRole = findRole(roles, "animal crossing");
                if (animalCrossingRole != null) removeRoleFromMember(guild, member, animalCrossingRole);
                break;
            case "🥚":
                Role pokemonRole = findRole(roles, "pokemon");
                if (pokemonRole != null) removeRoleFromMember(guild, member, pokemonRole);
                break;
            case "⚔":
                Role runescapeRole = findRole(roles, "runescape");
                if (runescapeRole != null) removeRoleFromMember(guild, member, runescapeRole);
                break;
            case "🍿":
                Role movieStreamsRole = findRole(roles, "movie streams");
                if (movieStreamsRole != null) removeRoleFromMember(guild, member, movieStreamsRole);
                break;
            case "👾":
                Role gameStreamsRole = findRole(roles, "game streams");
                if (gameStreamsRole != null) removeRoleFromMember(guild, member, gameStreamsRole);
                break;
            default:
                event.getReaction().removeReaction().queue();
                break;
        }
    }

    private static Role findRole(List<Role> availableRoles, String roleName) {
        return availableRoles.stream().filter(role -> role.getName().toLowerCase().contains(roleName)).findFirst().orElse(null);
    }

    private static void addRoleToMember(Guild guild, Member member, Role role) {
        guild.addRoleToMember(member, role).queue();
    }

    private static void removeRoleFromMember(Guild guild, Member member, Role role) {
        guild.removeRoleFromMember(member, role).queue();
    }
}
