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

import net.kodehawa.mantarobot.commands.currency.profile.ProfileComponent;
import net.kodehawa.mantarobot.db.rel.help.Transactional;

public class UserProfileComponent extends Transactional {

    private int slot;
    private ProfileComponent which;
    private NewUser owner;

    public UserProfileComponent(){}

    public UserProfileComponent(int slot, ProfileComponent which, NewUser owner) {
        this.slot = slot;
        this.which = which;
        this.owner = owner;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ProfileComponent getWhich() {
        return which;
    }

    public void setWhich(ProfileComponent which) {
        this.which = which;
    }

    public NewUser getOwner() {
        return owner;
    }

    public void setOwner(NewUser owner) {
        this.owner = owner;
    }
}
