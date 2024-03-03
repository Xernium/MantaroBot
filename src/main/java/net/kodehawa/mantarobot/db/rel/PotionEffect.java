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

import net.kodehawa.mantarobot.commands.currency.item.ItemType;
import net.kodehawa.mantarobot.db.rel.help.Transactional;

public class PotionEffect extends Transactional {
    private NewUser appliedTo;
    private long until;
    private ItemType.PotionType type;
    private int timesUsed;
    private int amountEquipped;

    public PotionEffect() {}

    public PotionEffect(NewUser appliedTo, long until, ItemType.PotionType type, int timesUsed, int amountEquipped) {
        this.appliedTo = appliedTo;
        this.until = until;
        this.type = type;
        this.timesUsed = timesUsed;
        this.amountEquipped = amountEquipped;
    }

    public boolean use() {
        long newAmount = amountEquipped - 1;
        if (newAmount < 1) {
            return false;
        } else {
            setAmountEquipped(newAmount);
            setTimesUsed(0);
            return true;
        }
    }

    public void equip(int amount) {
        int newAmount = amountEquipped + amount;
        if (newAmount > 15) {
            setAmountEquipped(15);
        } else {
            setAmountEquipped(newAmount);
        }
    }

    public void equip() {
        equip(1);
    }

    public NewUser getAppliedTo() {
        return appliedTo;
    }

    public void setAppliedTo(NewUser appliedTo) {
        this.appliedTo = appliedTo;
    }

    public long getUntil() {
        return this.until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public ItemType.PotionType getType() {
        return this.type;
    }

    public void setType(ItemType.PotionType type) {
        this.type = type;
    }

    public long getTimesUsed() {
        return this.timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public long getAmountEquipped() {
        return this.amountEquipped;
    }

    public void setAmountEquipped(int amountEquipped) {
        this.amountEquipped = amountEquipped;
    }
}
