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

import net.kodehawa.mantarobot.commands.currency.item.ItemStack;
import net.kodehawa.mantarobot.db.rel.help.Transactional;

public class InventoryItem extends Transactional {
    private ItemStack itemStack;
    private NewUser owner;

    public InventoryItem(long id, ItemStack itemStack, NewUser owner) {
        this.itemStack = itemStack;
        this.owner = owner;
    }

    public NewUser getOwner() {
        return owner;
    }

    public void setOwner(NewUser owner) {
        this.owner = owner;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
