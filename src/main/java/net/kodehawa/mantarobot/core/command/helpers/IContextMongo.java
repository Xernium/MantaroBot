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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.attribute.IAgeRestrictedChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.kodehawa.mantarobot.MantaroBot;
import net.kodehawa.mantarobot.core.command.i18n.I18nContext;
import net.kodehawa.mantarobot.data.Config;
import net.kodehawa.mantarobot.dbold.ManagedDatabase;
import net.kodehawa.mantarobot.dbold.entities.MantaroObject;
import net.kodehawa.mantarobot.dbold.entities.Marriage;
import net.kodehawa.mantarobot.dbold.entities.MongoGuild;
import net.kodehawa.mantarobot.dbold.entities.MongoUser;
import net.kodehawa.mantarobot.dbold.entities.Player;
import net.kodehawa.mantarobot.dbold.entities.PlayerStats;
import net.kodehawa.mantarobot.utils.commands.UtilsContext;
import net.kodehawa.mantarobot.utils.commands.ratelimit.RateLimitContext;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Collection;

@SuppressWarnings("unused")
public interface IContextMongo extends IContextBase {

    ManagedDatabase db();
    MantaroObject getMantaroData();

    default Player getPlayer() {
        return db().getPlayer(getAuthor());
    }

    default Player getPlayer(User user) {
        return db().getPlayer(user);
    }

    default MongoUser getDBUser() {
        return db().getUser(getAuthor());
    }

    default MongoUser getDBUser(User user) {
        return db().getUser(user);
    }

    default MongoGuild getDBGuild() {
        return db().getGuild(getGuild());
    }

    default Marriage getMarriage(@NotNull MongoUser userData) {
        return db().getMarriage(userData.getMarriageId());
    }

    default PlayerStats getPlayerStats() {
        return db().getPlayerStats(getMember());
    }

    default PlayerStats getPlayerStats(String id) {
        return db().getPlayerStats(id);
    }

    default PlayerStats getPlayerStats(User user) {
        return db().getPlayerStats(user);
    }

    default PlayerStats getPlayerStats(Member member) {
        return db().getPlayerStats(member);
    }

    default MongoUser getDBUser(Member member) {
        return db().getUser(member);
    }

    default MongoUser getDBUser(String id) {
        return db().getUser(id);
    }

    default Player getPlayer(Member member) {
        return db().getPlayer(member);
    }

    default Player getPlayer(String id) {
        return db().getPlayer(id);
    }

    default I18nContext getGuildLanguageContext() {
        return new I18nContext(getDBGuild(), null);
    }


}

