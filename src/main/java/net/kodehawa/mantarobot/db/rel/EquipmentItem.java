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

import net.kodehawa.mantarobot.commands.currency.item.Item;
import net.kodehawa.mantarobot.db.entities.done.PlayerEquipment;
import net.kodehawa.mantarobot.db.rel.help.Transactional;

public class EquipmentItem extends Transactional {

    // Key: Owner + type
    private Item item;
    private NewUser owner;
    private int durability;
    private PlayerEquipment.EquipmentType type;
    private boolean autoEquip;

    public EquipmentItem(){}

    public EquipmentItem(Item item, NewUser owner, int durability, PlayerEquipment.EquipmentType type, boolean autoEquip) {
        this.item = item;
        this.owner = owner;
        this.durability = durability;
        this.type = type;
        this.autoEquip = autoEquip;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public NewUser getOwner() {
        return owner;
    }

    public void setOwner(NewUser owner) {
        this.owner = owner;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public PlayerEquipment.EquipmentType getType() {
        return type;
    }

    public void setType(PlayerEquipment.EquipmentType type) {
        this.type = type;
    }

    public boolean isAutoEquip() {
        return autoEquip;
    }

    public void setAutoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
    }
}
