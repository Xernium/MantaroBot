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

package net.kodehawa.mantarobot.commands.currency.item;

import com.google.common.collect.ImmutableList;
import net.kodehawa.mantarobot.commands.currency.item.special.tools.Axe;
import net.kodehawa.mantarobot.commands.currency.item.special.tools.FishRod;
import net.kodehawa.mantarobot.commands.currency.item.special.tools.Pickaxe;
import net.kodehawa.mantarobot.commands.currency.item.special.helpers.Breakable;
import net.kodehawa.mantarobot.commands.currency.item.special.tools.Wrench;
import net.kodehawa.mantarobot.db.ManagedObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class PlayerEquipment {
    //int = itemId

    public static class PlayerEquippedItem implements ManagedObject, EquipmentTyped {
        private long id;
        private final EquipmentType type;
        private final int itemId;
        private int durability;

        public PlayerEquippedItem(long id, EquipmentType type, int itemId, int durability) {
            this.id = id;
            this.type = type;
            this.itemId = itemId;
            this.durability = durability;
        }

        public int getItemId() {
            return itemId;
        }

        public int getDurability() {
            return durability;
        }

        public void setDurability(int durability) {
            this.durability = durability;
        }

        @NotNull
        @Override
        public String getTableName() {
            return "PlayerEquippedItem";
        }

        @Override
        public long getIdLong() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public EquipmentType getEquipmentType() {
            return type;
        }

        // Small hack for Set
        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o instanceof PlayerEquippedItem p) {
                return p.type == this.type;
            }
            return false;
        }
    }

    public static interface EquipmentTyped {
        EquipmentType getEquipmentType();
    }


    private final Set<PlayerEquippedItem> equipped;
    private final Set<PotionEffect> effects;
    public PlayerEquipment(Set<PlayerEquippedItem> equipped, Set<PotionEffect> effects) {
        this.equipped = equipped;
        this.effects = effects;
    }

    public boolean equipItem(Item item) {
        EquipmentType type = getTypeFor(item);
        if (type == null || type.getType() != 0) {
            return false;
        }
        equipped.add(new PlayerEquippedItem(-1, type, ItemHelper.idOf(item), item instanceof Breakable b ? b.getMaxDurability() : -1));

        return true;
    }

    public void applyEffect(PotionEffect effect) {
        EquipmentType type = getTypeFor(ItemHelper.fromId(effect.getPotion()));
        if (type == null || type.getType() != 1) {
            return;
        }
        // Doesnt collide due to small hack in PotionEffect .equals()
        effects.add(effect);
    }

    //Convenience methods start here.
    public void resetOfType(EquipmentType type) {
        find(type, equipped).ifPresent(equipped::remove);
    }

    public void resetEffect(EquipmentType type) {
        find(type, effects).ifPresent(effects::remove);
    }

    public static <T extends EquipmentTyped> Optional<T> find(EquipmentType search, Collection<T> contained){
        return ImmutableList.copyOf(contained).stream().filter(el -> {return el.getEquipmentType() == search;}).findFirst();
    }
    public void incrementEffectUses(EquipmentType type) {
        find(type, effects).ifPresent(active -> active.setTimesUsed(active.getTimesUsed() + 1));
    }

    public boolean isEffectActive(EquipmentType type, int maxUses) {
        Optional<PotionEffect> find = find(type, effects);
        if (find.isPresent()) {
            PotionEffect effect = find.get();
            return effect.getTimesUsed() < maxUses;
        }
        return false;
    }

    public PotionEffect getCurrentEffect(EquipmentType type) {
        return find(type, effects).orElse(null);
    }

    public Item getEffectItem(EquipmentType type) {
        PotionEffect effect = find(type, effects).orElse(null);
        return effect == null ? null : ItemHelper.fromId(effect.getPotion());
    }

    public Integer of(EquipmentType type) {
        return find(type, equipped).map(PlayerEquippedItem::getItemId).orElse(0);
    }

    public static EquipmentType getTypeFor(Item item) {
        for (EquipmentType type : EquipmentType.values()) {
            if (type.getPredicate().test(item)) {
                return type;
            }
        }

        return null;
    }

    public void resetDurabilityTo(EquipmentType type, int amount) {
        find(type, equipped).ifPresent(item -> item.setDurability(amount));
    }

    public int reduceDurability(EquipmentType type, int amount) {
        Optional<PlayerEquippedItem> item = find(type, equipped);
        if (item.isPresent()) {
            item.get().setDurability(item.get().getDurability() - amount);
            return item.get().getDurability();
        }
        return 0;
    }

    public boolean containsItem(EquipmentType type) {
        return find(type, equipped).isPresent();
    }

    public int getItem(EquipmentType type) {
        return find(type, equipped).map(PlayerEquippedItem::getItemId).orElse(-1);
    }

    public Set<PlayerEquippedItem> getEquipment() {
        return this.equipped;
    }

    public Set<PotionEffect> getEffects() {
        return this.effects;
    }

    public enum EquipmentType {
        ROD(FishRod.class::isInstance, 0),
        PICK(Pickaxe.class::isInstance, 0),
        AXE(Axe.class::isInstance, 0),
        WRENCH(Wrench.class::isInstance, 0),
        POTION(item -> item.getItemType() == ItemType.POTION, 1),
        BUFF(item -> item.getItemType() == ItemType.BUFF, 1);

        private final Predicate<Item> predicate;
        private final int type;

        EquipmentType(Predicate<Item> predicate, int type) {
            this.predicate = predicate;
            this.type = type;
        }

        public static EquipmentType fromString(String text) {
            for (EquipmentType b : EquipmentType.values()) {
                if (b.name().equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }

        public Predicate<Item> getPredicate() {
            return this.predicate;
        }

        public int getType() {
            return this.type;
        }
    }
}
