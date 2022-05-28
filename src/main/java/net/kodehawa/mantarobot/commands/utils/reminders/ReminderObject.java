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

import java.util.UUID;

//This just exists for the sake of serializing (!)
public class ReminderObject {
    public final UUID id;
    public final String reminder;
    public final long time;
    private final long scheduledAtMillis;
    private final ISnowflake userId;
    private final ISnowflake guildId;

    ReminderObject(UUID id, String reminder, long time,
                   long scheduledAtMillis, ISnowflake userId, ISnowflake guildId) {
        this.id = id;
        this.reminder = reminder;
        this.time = time;
        this.scheduledAtMillis = scheduledAtMillis;
        this.userId = userId;
        this.guildId = guildId;
    }

    public static ReminderObjectBuilder builder() {
        return new ReminderObjectBuilder();
    }

    public UUID getId() {
        return this.id;
    }

    public String getReminder() {
        return this.reminder;
    }

    public long getTime() {
        return this.time;
    }

    public long getScheduledAtMillis() {
        return this.scheduledAtMillis;
    }

    public ISnowflake getUserId() {
        return this.userId;
    }

    public ISnowflake getGuildId() {
        return this.guildId;
    }

    public static class ReminderObjectBuilder {
        private UUID id;
        private String reminder;
        private long time;
        private long scheduledAtMillis;
        private ISnowflake userId;
        private ISnowflake guildId;

        ReminderObjectBuilder() { }

        public ReminderObject.ReminderObjectBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReminderObject.ReminderObjectBuilder reminder(String reminder) {
            this.reminder = reminder;
            return this;
        }

        public ReminderObject.ReminderObjectBuilder time(long time) {
            this.time = time;
            return this;
        }

        public ReminderObject.ReminderObjectBuilder scheduledAtMillis(long scheduledAtMillis) {
            this.scheduledAtMillis = scheduledAtMillis;
            return this;
        }

        public ReminderObject.ReminderObjectBuilder userId(ISnowflake userId) {
            this.userId = userId;
            return this;
        }

        public ReminderObject.ReminderObjectBuilder guildId(ISnowflake guildId) {
            this.guildId = guildId;
            return this;
        }

        public ReminderObject build() {
            return new ReminderObject(id, reminder, time, scheduledAtMillis, userId, guildId);
        }
    }
}
