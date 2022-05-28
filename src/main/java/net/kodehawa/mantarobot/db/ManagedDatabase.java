/*
 * Copyright (C) 2016-2021 David Rubio Escares / Kodehawa
 *
 *  Mantaro is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  Mantaro is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro. If not, see http://www.gnu.org/licenses/
 */

package net.kodehawa.mantarobot.db;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.kodehawa.mantarobot.commands.currency.seasons.Season;
import net.kodehawa.mantarobot.commands.currency.seasons.SeasonPlayer;
import net.kodehawa.mantarobot.db.entities.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class ManagedDatabase {

    @Nullable
    @CheckReturnValue
    public abstract CustomCommand getCustomCommand(@Nonnull ISnowflake guildId, @Nonnull String name);


    @Nullable
    @CheckReturnValue
    public final CustomCommand getCustomCommand(@Nonnull GuildMessageReceivedEvent event, @Nonnull String cmd) {
        return getCustomCommand(event.getGuild(), cmd);
    }

    @Nonnull
    @CheckReturnValue
    public abstract List<CustomCommand> getCustomCommands();

    @Nonnull
    @CheckReturnValue
    public abstract List<CustomCommand> getCustomCommands(@Nonnull ISnowflake guildId);


    @Nonnull
    @CheckReturnValue
    public abstract List<CustomCommand> getCustomCommandsByName(@Nonnull String name);

    @Nonnull
    @CheckReturnValue
    public abstract DBGuild getGuild(@Nonnull ISnowflake guildId);


    @Nonnull
    @CheckReturnValue
    public final DBGuild getGuild(@Nonnull Member member) {
        return getGuild(member.getGuild());
    }

    @Nonnull
    @CheckReturnValue
    public final DBGuild getGuild(@Nonnull GuildMessageReceivedEvent event) {
        return getGuild(event.getGuild());
    }

    @Nonnull
    @CheckReturnValue
    public abstract MantaroObj getMantaroData();

    @Nonnull
    @CheckReturnValue
    public abstract Player getPlayer(@Nonnull ISnowflake userId);

    @Nonnull
    @CheckReturnValue
    public final Player getPlayer(@Nonnull Member member) {
        return getPlayer(member.getUser());
    }

    @Nonnull
    @CheckReturnValue
    public abstract SeasonPlayer getPlayerForSeason(@Nonnull ISnowflake userId, Season season);


    @Nonnull
    @CheckReturnValue
    public final SeasonPlayer getPlayerForSeason(@Nonnull Member member, Season season) {
        return getPlayerForSeason(member.getUser(), season);
    }

    @CheckReturnValue
    public abstract long getAmountSeasonalPlayers();

    @Nonnull
    @CheckReturnValue
    public abstract PlayerStats getPlayerStats(@Nonnull ISnowflake userId);

    @Nonnull
    @CheckReturnValue
    public final PlayerStats getPlayerStats(@Nonnull Member member) {
        return getPlayerStats(member.getUser());
    }

    @Nonnull
    @CheckReturnValue
    public abstract List<Player> getPlayers();

    //Can be null and it's perfectly valid.
    public abstract Marriage getMarriage(UUID marriageId);

    @Nonnull
    @CheckReturnValue
    public abstract List<Marriage> getMarriages();

    @Nonnull
    @CheckReturnValue
    public abstract List<PremiumKey> getPremiumKeys();

    //Also tests if the key is valid or not!
    @Nullable
    @CheckReturnValue
    public abstract PremiumKey getPremiumKey(@Nullable UUID id);

    @Nonnull
    @CheckReturnValue
    public abstract DBUser getUser(@Nonnull ISnowflake userId);

    @Nonnull
    @CheckReturnValue
    public final DBUser getUser(@Nonnull Member member) {
        return getUser(member.getUser());
    }

    public abstract void save(@Nonnull ManagedObject object);

    public abstract void saveUpdating(@Nonnull ManagedObject object);

    public abstract void delete(@Nonnull ManagedObject object);
}
