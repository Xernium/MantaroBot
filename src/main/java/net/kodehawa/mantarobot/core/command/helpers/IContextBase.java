/*
 * Copyright (C) 2016 Kodehawa
 *
 * Mantaro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mantaro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro. If not, see http://www.gnu.org/licenses/
 *
 */

package net.kodehawa.mantarobot.core.command.helpers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.attribute.IAgeRestrictedChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.kodehawa.mantarobot.MantaroBot;
import net.kodehawa.mantarobot.core.command.i18n.I18nContext;
import net.kodehawa.mantarobot.data.Config;
import net.kodehawa.mantarobot.dbold.ManagedDatabase;
import net.kodehawa.mantarobot.dbold.entities.*;
import net.kodehawa.mantarobot.utils.commands.UtilsContext;
import net.kodehawa.mantarobot.utils.commands.ratelimit.RateLimitContext;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

@SuppressWarnings("unused")
public interface IContextBase {
    Guild getGuild();
    GuildMessageChannel getChannel();
    Member getMember();
    Member getSelfMember();
    User getAuthor();
    User getSelfUser();
    RateLimitContext ratelimitContext();
    UtilsContext getUtilsContext();
    I18nContext getLanguageContext();

    void send(String s);
    void send(MessageCreateData message);
    void sendStripped(String s);
    void send(MessageEmbed e);
    void send(MessageEmbed e, ActionRow... actionRows);
    void sendLocalized(String s, Object... args);
    void sendLocalizedStripped(String s, Object... args);
    void sendFormat(String message, Object... format);
    void sendFormatStripped(String message, Object... format);
    void sendFormat(String message, Collection<ActionRow> actionRow, Object... format);
    Message sendResult(String s);
    Message sendResult(MessageEmbed e);

    ShardManager getShardManager();
    Config getConfig();

    default MantaroBot getBot() {
        return MantaroBot.getInstance();
    }


    default Color getMemberColor(@NotNull Member member) {
        return member.getColors().getPrimary() == null ? Color.PINK : member.getColors().getPrimary();
    }

    default Color getMemberColor() {
        return getMemberColor(getMember());
    }


    default boolean isChannelNSFW() {
        if (getChannel() instanceof IAgeRestrictedChannel txtChannel) {
            return txtChannel.isNSFW();
        }

        return true;
    }

    default EmbedBuilder baseEmbed(@NotNull IContextBase ctx, String name, String image) {
        return new EmbedBuilder()
                .setAuthor(name, null, image)
                .setColor(ctx.getMember().getColors().getPrimary())
                .setFooter("Requested by: %s".formatted(ctx.getMember().getEffectiveName()),
                        ctx.getGuild().getIconUrl()
                );
    }

}

