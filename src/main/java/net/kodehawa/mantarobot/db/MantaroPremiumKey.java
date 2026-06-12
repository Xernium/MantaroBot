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

package net.kodehawa.mantarobot.db;

import jakarta.persistence.*;

import javax.annotation.Nullable;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "PremiumKeys")
public class MantaroPremiumKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Duration duration;
    private boolean enabled;
    private Instant start;

    //private MantaroUser keyOwner;
    private Type type;


    public MantaroPremiumKey() {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "MantaroPremiumKey{" +
                "id=" + id +
                ", duration=" + duration +
                ", enabled=" + enabled +
                ", start=" + start +
                ", type=" + type +
                '}';
    }

    public static enum Type {
        GUILD,
        USER
    }

}
