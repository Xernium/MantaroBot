/*
 * Copyright (C) 2024 FivePB (Xernium)
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

public class Waifu extends Transactional {
    // key: user + claimed
    private NewUser owner;
    private long valuePaid;
    private NewUser claimed;

    public Waifu(){}

    public Waifu(NewUser owner, long valuePaid, NewUser claimed) {
        this.owner = owner;
        this.valuePaid = valuePaid;
        this.claimed = claimed;
    }

    public NewUser getOwner() {
        return owner;
    }

    public long getValuePaid() {
        return valuePaid;
    }

    public NewUser getClaimed() {
        return claimed;
    }

    public void setOwner(NewUser owner) {
        this.owner = owner;
    }

    public void setValuePaid(long valuePaid) {
        this.valuePaid = valuePaid;
    }

    public void setClaimed(NewUser claimed) {
        this.claimed = claimed;
    }
}
