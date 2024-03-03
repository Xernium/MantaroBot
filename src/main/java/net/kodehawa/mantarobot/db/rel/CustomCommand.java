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

package net.kodehawa.mantarobot.db.rel;

import net.kodehawa.mantarobot.db.rel.help.Transactional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomCommand extends Transactional {
    private String name;
    private NewGuild owner;
    private boolean nsfw;
    private boolean locked;

    public CustomCommand(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NewGuild getOwner() {
        return owner;
    }

    public void setOwner(NewGuild owner) {
        this.owner = owner;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
