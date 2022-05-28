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

package net.kodehawa.mantarobot.commands.utils.reminders;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.db.ManagedDatabase;
import net.kodehawa.mantarobot.db.entities.helpers.DummySnowflake;
import net.kodehawa.mantarobot.utils.Pair;
import org.json.JSONObject;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class Reminder {
    private static final String table = "reminder";
    private static final String ztable = "zreminder";
    private static final JedisPool pool = MantaroData.getDefaultJedisPool();
    private static final ManagedDatabase db = MantaroData.db();

    public final UUID id;
    public final String reminder;

    //When should we fire this.
    public final long time;

    private final long scheduledAtMillis;
    private final ISnowflake userId;
    private final ISnowflake guildId;

    private Reminder(UUID id, ISnowflake userId, ISnowflake guildId, String reminder, long scheduledAt, long time) {
        this.id = id;
        this.userId = userId;
        this.guildId = guildId;
        this.reminder = reminder;
        this.time = time;
        this.scheduledAtMillis = scheduledAt;
    }

    private static Pair<ISnowflake, UUID> fromIdentifier(String identifier) {
        var sp = identifier.split(":");
        return new Pair<>(new DummySnowflake(sp[0]), UUID.fromString(sp[1]));
    }

    public static String toIdentifier(ISnowflake userId, UUID reminderId) {
        return userId.getIdLong() + ":" + reminderId.toString();
    }

    public static void cancel(ISnowflake userId, UUID reminderId, CancelReason reason) {
        var fullId = toIdentifier(userId, reminderId);
        try (var redis = pool.getResource()) {
            var data = redis.hget(table, fullId);

            redis.zrem(ztable, data);
            redis.hdel(table, fullId);
        }

        var user = db.getUser(userId);
        user.getReminders().remove(reminderId);

        if (reason == CancelReason.REMINDED) {
            user.incrementReminders();
        }

        user.save();
    }

    public void schedule() {
        var r = new JSONObject()
                .put("id", id.toString())
                .put("user", userId.getIdLong())
                .put("guild", guildId.getIdLong())
                .put("scheduledAt", scheduledAtMillis)
                .put("reminder", reminder)
                .put("at", time);

        try (var redis = pool.getResource()) {
            redis.zadd(ztable, time, r.toString());
            //Needed for removal.
            redis.hset(table, toIdentifier(userId, id), r.toString());
        }

        var user = db.getUser(userId);

        var sp =
        user.getReminders().add(id);
        user.save();
    }

    public static class Builder {
        private long current;
        private String reminder;
        private long time;
        private ISnowflake userId;
        private ISnowflake guildId;

        public Builder id(ISnowflake id) {
            userId = id;
            return this;
        }

        public Builder reminder(String reminder) {
            this.reminder = reminder;
            return this;
        }

        public Builder time(long to) {
            time = to;
            return this;
        }

        public Builder current(long start) {
            current = start;
            return this;
        }

        public Builder guild(ISnowflake id) {
            guildId = id;
            return this;
        }

        public Reminder build() {
            if (userId == null)
                throw new IllegalArgumentException("User ID cannot be null");
            if (reminder == null)
                throw new IllegalArgumentException("Reminder cannot be null");
            if (guildId == null)
                throw new IllegalArgumentException("Guild ID cannot be null");
            if (time <= 0)
                throw new IllegalArgumentException("Time to remind must be positive and >0");
            if (current <= 0)
                throw new IllegalArgumentException("Current time must be positive and >0");

            return new Reminder(UUID.randomUUID(), userId, guildId, reminder, current, time);
        }
    }

    public enum CancelReason {
        CANCEL, REMINDED, ERROR_DELIVERING
    }
}
