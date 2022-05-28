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

import com.rethinkdb.model.OptArgs;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;
import net.kodehawa.mantarobot.ExtraRuntimeOptions;
import net.kodehawa.mantarobot.commands.currency.seasons.Season;
import net.kodehawa.mantarobot.commands.currency.seasons.SeasonPlayer;
import net.kodehawa.mantarobot.db.entities.*;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

public class JdbcDatabase extends ManagedDatabase {
    private static final Logger log = LoggerFactory.getLogger(JdbcDatabase.class);
    private final Connection conn = null;
    private final Jdbi jdbi;

    public JdbcDatabase(@Nonnull Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    private static void log(String message, Object... fmtArgs) {
        if (ExtraRuntimeOptions.LOG_DB_ACCESS) {
            log.info(message, fmtArgs);
        }
    }

    private static void log(String message) {
        if (ExtraRuntimeOptions.LOG_DB_ACCESS) {
            log.info(message);
        }
    }

    @Nullable
    @CheckReturnValue
    @Override
    public CustomCommand getCustomCommand(@Nonnull String guildId, @Nonnull String name) {
        log("Requesting custom command {}:{} from rdb", guildId, name);
        // Table: "Commands"
        //      pk: INTEGER NOT NULL AUTOINCREMENT UNIQUE
        //      fk: guild.id
        //      VARCHAR(64) name
        //      fk: user.id
        //      bool: nsfw
        //      bool: locked
        // Storage table: "GuildCommands"
        //      pk: INTEGER NOT NULL AUTOINCREMENT UNIQUE
        //      fk: commands.id
        return jdbi.withHandle(handle -> {
            handle.createQuery("SELECT * FROM Commands WHERE Guild.ID = ? AND Commands.name = ?")
                    .bind(guildId, name)
                    .mapToMap();


        });
        return r.table(CustomCommand.DB_TABLE).get(guildId + ":" + name).runAtom(conn, CustomCommand.class);
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public List<CustomCommand> getCustomCommands() {
        log("Requesting all custom commands from rdb");
        Result<CustomCommand> c = r.table(CustomCommand.DB_TABLE).run(conn, CustomCommand.class);
        return c.toList();
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public List<CustomCommand> getCustomCommands(@Nonnull String guildId) {
        log("Requesting all custom commands from guild {} from rdb", guildId);
        Result<CustomCommand> c = r.table(CustomCommand.DB_TABLE)
                .getAll(guildId)
                .optArg("index", "guild")
                .run(conn, CustomCommand.class);
        return c.toList();
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public List<CustomCommand> getCustomCommandsByName(@Nonnull String name) {
        log("Requesting all custom commands named {} from rdb", name);
        String pattern = ':' + name + '$';
        Result<CustomCommand> c = r.table(CustomCommand.DB_TABLE).filter(quote -> quote.g("id").match(pattern)).run(conn, CustomCommand.class);
        return c.toList();
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public DBGuild getGuild(@Nonnull String guildId) {
        log("Requesting guild {} from rdb", guildId);
        DBGuild guild = r.table(DBGuild.DB_TABLE).get(guildId).runAtom(conn, DBGuild.class);
        return guild == null ? DBGuild.of(guildId) : guild;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public MantaroObj getMantaroData() {
        log("Requesting MantaroObj from rdb");
        MantaroObj obj = r.table(MantaroObj.DB_TABLE).get("mantaro").runAtom(conn, MantaroObj.class);
        return obj == null ? MantaroObj.create() : obj;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public Player getPlayer(@Nonnull String userId) {
        log("Requesting player {} from rdb", userId);
        Player player = r.table(Player.DB_TABLE).get(userId + ":g").runAtom(conn, Player.class);
        return player == null ? Player.of(userId) : player;
    }
    @Nonnull
    @CheckReturnValue
    @Override
    public SeasonPlayer getPlayerForSeason(@Nonnull String userId, Season season) {
        log("Requesting player {} (season {}) from rdb", userId, season);
        SeasonPlayer player = r.table(SeasonPlayer.DB_TABLE).get(userId + ":" + season).runAtom(conn, SeasonPlayer.class);
        return player == null ? SeasonPlayer.of(userId, season) : player;
    }
    @CheckReturnValue
    @Override
    public long getAmountSeasonalPlayers() {
        return r.table(SeasonPlayer.DB_TABLE).count().runAtom(conn, OptArgs.of("read_mode", "outdated"), Long.class);
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public PlayerStats getPlayerStats(@Nonnull String userId) {
        log("Requesting player STATS {} from rdb", userId);
        PlayerStats playerStats = r.table(PlayerStats.DB_TABLE).get(userId).runAtom(conn, PlayerStats.class);
        return playerStats == null ? PlayerStats.of(userId) : playerStats;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public List<Player> getPlayers() {
        log("Requesting all players from rdb");
        String pattern = ":g$";
        Result<Player> c = r.table(Player.DB_TABLE).filter(quote -> quote.g("id").match(pattern)).run(conn, Player.class);
        return c.toList();
    }

    //Can be null and it's perfectly valid.
    @Override
    public Marriage getMarriage(String marriageId) {
        if (marriageId == null) {
            return null;
        }

        log("Requesting marriage {} from rdb", marriageId);
        return r.table(Marriage.DB_TABLE).get(marriageId).runAtom(conn, Marriage.class);
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public List<Marriage> getMarriages() {
        log("Requesting all marriages from rdb");
        Result<Marriage> c = r.table(Marriage.DB_TABLE).run(conn, Marriage.class);
        return c.toList();
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public List<PremiumKey> getPremiumKeys() {
        log("Requesting all premium keys from rdb");
        Result<PremiumKey> c = r.table(PremiumKey.DB_TABLE).run(conn, PremiumKey.class);
        return c.toList();
    }

    //Also tests if the key is valid or not!
    @Nullable
    @CheckReturnValue
    @Override
    public PremiumKey getPremiumKey(@Nullable String id) {
        log("Requesting premium key {} from rdb", id);
        if (id == null) return null;
        return r.table(PremiumKey.DB_TABLE).get(id).runAtom(conn, PremiumKey.class);
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public DBUser getUser(@Nonnull String userId) {
        log("Requesting user {} from rdb", userId);
        DBUser user = r.table(DBUser.DB_TABLE).get(userId).runAtom(conn, DBUser.class);
        return user == null ? DBUser.of(userId) : user;
    }

    @Override
    public void save(@Nonnull ManagedObject object) {
        log("Saving {} {}:{} to rethink (replacing)", object.getClass().getSimpleName(), object.getTableName(), object.getDatabaseId());

        r.table(object.getTableName())
                .insert(object)
                .optArg("conflict", "replace")
                .runNoReply(conn);
    }

    @Override
    public void saveUpdating(@Nonnull ManagedObject object) {
        log("Saving {} {}:{} to rethink (updating)", object.getClass().getSimpleName(), object.getTableName(), object.getDatabaseId());

        r.table(object.getTableName())
                .insert(object)
                .optArg("conflict", "update")
                .runNoReply(conn);
    }

    @Override
    public void delete(@Nonnull ManagedObject object) {
        log("Deleting {} {}:{} from rdb", object.getClass().getSimpleName(), object.getTableName(), object.getDatabaseId());

        r.table(object.getTableName())
                .get(object.getId())
                .delete()
                .runNoReply(conn);
    }
}
