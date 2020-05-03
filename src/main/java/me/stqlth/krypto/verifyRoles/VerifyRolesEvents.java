package me.stqlth.krypto.verifyRoles;

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
        Member target = event.getMember();
        if (target == null) return;

        switch (event.getReactionEmote().getName()) {
            case "😂":
                Role memesRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("memes")).findFirst().orElse(null);
                if (memesRole != null) event.getGuild().addRoleToMember(target, memesRole).queue();
                break;
            case "🎮":
                Role gamesRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("games")).findFirst().orElse(null);
                if (gamesRole != null) event.getGuild().addRoleToMember(target, gamesRole).queue();
                break;
            case "🎵":
                Role musicRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("music")).findFirst().orElse(null);
                if (musicRole != null) event.getGuild().addRoleToMember(target, musicRole).queue();
                break;
            case "💢":
                Role animeRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("anime")).findFirst().orElse(null);
                if (animeRole != null) event.getGuild().addRoleToMember(target, animeRole).queue();
                break;
            case "🐱":
                Role animalsRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("animals")).findFirst().orElse(null);
                if (animalsRole != null) event.getGuild().addRoleToMember(target, animalsRole).queue();
                break;
            case "🛑":
                Role nnsfwRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("nnsfw")).findFirst().orElse(null);
                if (nnsfwRole != null) event.getGuild().addRoleToMember(target, nnsfwRole).queue();
                break;
            case "🎭":
                Role roleplayRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("roleplay")).findFirst().orElse(null);
                if (roleplayRole != null) event.getGuild().addRoleToMember(target, roleplayRole).queue();
                break;
            case "⛏":
                Role minecraftRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("minecraft")).findFirst().orElse(null);
                if (minecraftRole != null) event.getGuild().addRoleToMember(target, minecraftRole).queue();
                break;
            case "🍂":
                Role animalCrossingRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("animal crossing")).findFirst().orElse(null);
                if (animalCrossingRole != null) event.getGuild().addRoleToMember(target, animalCrossingRole).queue();
                break;
            case "🥚":
                Role pokemonRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("pokemon")).findFirst().orElse(null);
                if (pokemonRole != null) event.getGuild().addRoleToMember(target, pokemonRole).queue();
                break;
            case "⚔":
                Role runescapeRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("runescape")).findFirst().orElse(null);
                if (runescapeRole != null) event.getGuild().addRoleToMember(target, runescapeRole).queue();
                break;
            default:
                event.getReaction().removeReaction().queue();
                break;
        }
    }

    private static void removeCategoryRoles (MessageReactionRemoveEvent event) {
        Member target = event.getMember();
        if (target == null) return;

        switch (event.getReactionEmote().getName()) {
            case "😂":
                Role memesRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("memes")).findFirst().orElse(null);
                if (memesRole != null) event.getGuild().removeRoleFromMember(target, memesRole).queue();
                break;
            case "🎮":
                Role gamesRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("games")).findFirst().orElse(null);
                if (gamesRole != null) event.getGuild().removeRoleFromMember(target, gamesRole).queue();
                break;
            case "🎵":
                Role musicRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("music")).findFirst().orElse(null);
                if (musicRole != null) event.getGuild().removeRoleFromMember(target, musicRole).queue();
                break;
            case "💢":
                Role animeRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("anime")).findFirst().orElse(null);
                if (animeRole != null) event.getGuild().removeRoleFromMember(target, animeRole).queue();
                break;
            case "🐱":
                Role animalsRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("animals")).findFirst().orElse(null);
                if (animalsRole != null) event.getGuild().removeRoleFromMember(target, animalsRole).queue();
                break;
            case "🛑":
                Role nnsfwRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("nnsfw")).findFirst().orElse(null);
                if (nnsfwRole != null) event.getGuild().removeRoleFromMember(target, nnsfwRole).queue();
                break;
            case "🎭":
                Role roleplayRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("roleplay")).findFirst().orElse(null);
                if (roleplayRole != null) event.getGuild().removeRoleFromMember(target, roleplayRole).queue();
                break;
            case "⛏":
                Role minecraftRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("minecraft")).findFirst().orElse(null);
                if (minecraftRole != null) event.getGuild().removeRoleFromMember(target, minecraftRole).queue();
                break;
            case "🍂":
                Role animalCrossingRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("animal crossing")).findFirst().orElse(null);
                if (animalCrossingRole != null) event.getGuild().removeRoleFromMember(target, animalCrossingRole).queue();
                break;
            case "🥚":
                Role pokemonRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("pokemon")).findFirst().orElse(null);
                if (pokemonRole != null) event.getGuild().removeRoleFromMember(target, pokemonRole).queue();
                break;
            case "⚔":
                Role runescapeRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("runescape")).findFirst().orElse(null);
                if (runescapeRole != null) event.getGuild().removeRoleFromMember(target, runescapeRole).queue();
                break;
            default:
                event.getReaction().removeReaction().queue();
                break;
        }
    }
}
