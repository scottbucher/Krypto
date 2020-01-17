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
            case "\uD83C\uDFAE":
                Role gamesRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("games")).findFirst().orElse(null);
                if (gamesRole != null) event.getGuild().addRoleToMember(target, gamesRole).queue();
                break;
            case "\uD83E\uDD5A":
                Role pokemonRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("pokémon")).findFirst().orElse(null);
                if (pokemonRole != null) event.getGuild().addRoleToMember(target, pokemonRole).queue();
                break;
            case "\uD83C\uDFAD":
                Role roleplayRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("roleplay")).findFirst().orElse(null);
                if (roleplayRole != null) event.getGuild().addRoleToMember(target, roleplayRole).queue();
                break;
            case "\uD83D\uDC15":
                Role animalsRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("animals")).findFirst().orElse(null);
                if (animalsRole != null) event.getGuild().addRoleToMember(target, animalsRole).queue();
                break;
            case "\u2B50":
                Role levelingRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("leveling")).findFirst().orElse(null);
                if (levelingRole != null) event.getGuild().addRoleToMember(target, levelingRole).queue();
                break;
            case "\uD83D\uDCCA":
                Role pollsRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("polls")).findFirst().orElse(null);
                if (pollsRole != null) event.getGuild().addRoleToMember(target, pollsRole).queue();
                break;
            case "\uD83C\uDF7F":
                Role movienightRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("movienight")).findFirst().orElse(null);
                if (movienightRole != null) event.getGuild().addRoleToMember(target, movienightRole).queue();
                break;
            case "\uD83D\uDCFA":
                Role streamRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("stream")).findFirst().orElse(null);
                if (streamRole != null) event.getGuild().addRoleToMember(target, streamRole).queue();
                break;
            case "\u26A0":
                Role nsfwRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("nsfw")).findFirst().orElse(null);
                if (nsfwRole != null) event.getGuild().addRoleToMember(target, nsfwRole).queue();
                break;
            case "\uD83C\uDFB5":
                Role musicRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("music")).findFirst().orElse(null);
                if (musicRole != null) event.getGuild().addRoleToMember(target, musicRole).queue();
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
            case "\uD83C\uDFAE":
                Role gamesRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("games")).findFirst().orElse(null);
                if (gamesRole != null) event.getGuild().removeRoleFromMember(target, gamesRole).queue();
                break;
            case "\uD83E\uDD5A":
                Role pokemonRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("pokémon")).findFirst().orElse(null);
                if (pokemonRole != null) event.getGuild().removeRoleFromMember(target, pokemonRole).queue();
                break;
            case "\uD83C\uDFAD":
                Role roleplayRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("roleplay")).findFirst().orElse(null);
                if (roleplayRole != null) event.getGuild().removeRoleFromMember(target, roleplayRole).queue();
                break;
            case "\uD83D\uDC15":
                Role animalsRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("animals")).findFirst().orElse(null);
                if (animalsRole != null) event.getGuild().removeRoleFromMember(target, animalsRole).queue();
                break;
            case "\u2B50":
                Role levelingRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("leveling")).findFirst().orElse(null);
                if (levelingRole != null) event.getGuild().removeRoleFromMember(target, levelingRole).queue();
                break;
            case "\uD83D\uDCCA":
                Role pollsRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("polls")).findFirst().orElse(null);
                if (pollsRole != null) event.getGuild().removeRoleFromMember(target, pollsRole).queue();
                break;
            case "\uD83C\uDF7F":
                Role movienightRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("movienight")).findFirst().orElse(null);
                if (movienightRole != null) event.getGuild().removeRoleFromMember(target, movienightRole).queue();
                break;
            case "\uD83D\uDCFA":
                Role streamRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("stream")).findFirst().orElse(null);
                if (streamRole != null) event.getGuild().removeRoleFromMember(target, streamRole).queue();
                break;
            case "\u26A0":
                Role nsfwRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("nsfw")).findFirst().orElse(null);
                if (nsfwRole != null) event.getGuild().removeRoleFromMember(target, nsfwRole).queue();
                break;
            case "\uD83C\uDFB5":
                Role musicRole = event.getGuild().getRoles().stream().filter(role -> role.getName().toLowerCase().contains("music")).findFirst().orElse(null);
                if (musicRole != null) event.getGuild().removeRoleFromMember(target, musicRole).queue();
                break;
            default:
                event.getReaction().removeReaction().queue();
                break;
        }
    }
}
