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

package net.kodehawa.mantarobot.db.entities;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.kodehawa.mantarobot.db.ManagedObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;;

public class CustomCommand implements ManagedObject {
    private final long id;
    private ISnowflake guildId;
    private String name;

    private ISnowflake owner;
    private boolean nsfw = false;
    private boolean locked = false;

    private final List<CustomGuildCommand> completions;

    public CustomCommand(long id, ISnowflake guildId, String name, ISnowflake owner, boolean nsfw, boolean locked, List<CustomGuildCommand> completions) {
        this.id = id;
        this.guildId = guildId;
        this.name = name;
        this.owner = owner;
        this.nsfw = nsfw;
        this.locked = locked;
        this.completions = completions;
    }

    public static CustomCommand of(ISnowflake guildId, String cmdName, List<CustomGuildCommand> responses) {
        return new CustomCommand(0, guildId, cmdName, null, false, false, new ArrayList<>());
    }

    public static CustomCommand transfer(ISnowflake guildId, CustomCommand command) {
        command.guildId = guildId;
        return command;
    }

    public ISnowflake getGuildId() {
        return guildId;
    }

    public String getName() {
        return name;
    }

    public List<CustomGuildCommand> getValues() {
        return completions;
    }

    @Override
    public long getIdLong() {
        return 0;
    }

    @Override
    @Nonnull
    public String getTableName() {
        return "Commands";
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setOwner(ISnowflake owner) {
        this.owner = owner;
    }

    public static class CustomGuildCommand implements ManagedObject{

        private final long id;
        private String value;

        public CustomGuildCommand(long id, String value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public long getIdLong() {
            return 0;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @NotNull
        @Override
        public String getTableName() {
            return "GuildCommands";
        }
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public boolean isLocked() {
        return locked;
    }

    public ISnowflake getOwner() {
        return owner;
    }
}
