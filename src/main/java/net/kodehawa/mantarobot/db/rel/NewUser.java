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
import net.kodehawa.mantarobot.commands.currency.profile.Badge;
import net.kodehawa.mantarobot.commands.currency.profile.inventory.InventorySortType;
import net.kodehawa.mantarobot.db.rel.help.Transactional;
import net.kodehawa.mantarobot.db.rel.help.WaifuState;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NewUser extends Transactional {

    private long id;
    private boolean blacklistedFromBot;
    private NewUser marriagePartner;
    private long balance;
    private int reputation;
    private int dailyStreak;
    private Item lastCrateGiven;
    private String description ;
    private Instant lastDailyAt;
    private int dustLevel; // in %
    private Instant lockedUntil; // Moderation
    private Badge activeBadge;
    private WaifuState waifuState = WaifuState.NORMAL;
    private boolean actionsDisabled;

    // Stats
    private long miningExperience;
    private long fishingExperience;
    private long chopExperience;
    private int timesMopped;
    private int cratesOpened;
    private int sharksCaught;
    private int gamesWon;
    private int marketUsed;
    private int remindedTimes;
    private int gambleWins;
    private int slotsWins;
    private long gambleWinAmount;
    private long slotsWinAmount;
    private int craftedItems;
    private int repairedItems;
    private int salvagedItems;
    private int toolsBroken;
    private int looted;
    private int mined;
    private long gambleLose;
    private long slotsLose;


    private int waifuSlots = 3;
    private int petSlots = 4; // = 4
    private boolean newPlayerNotice;
    private InventorySortType inventorySortApproach = InventorySortType.AMOUNT;

    private Date birthday;
    private TimeZone timeZone;
    private Locale locale;

    public NewUser(){}

    public NewUser(long id, boolean blacklistedFromBot, NewUser marriagePartner, long balance, int reputation, int dailyStreak, Item lastCrateGiven, String description, Instant lastDailyAt, int dustLevel, Instant lockedUntil, Badge activeBadge, WaifuState waifuState, boolean actionsDisabled, long miningExperience, long fishingExperience, long chopExperience, int timesMopped, int cratesOpened, int sharksCaught, int gamesWon, int marketUsed, int remindedTimes, int gambleWins, int slotsWins, long gambleWinAmount, long slotsWinAmount, int craftedItems, int repairedItems, int salvagedItems, int toolsBroken, int looted, int mined, long gambleLose, long slotsLose, int waifuSlots, int petSlots, boolean newPlayerNotice, InventorySortType inventorySortApproach, Date birthday, TimeZone timeZone, Locale locale) {
        this.id = id;
        this.blacklistedFromBot = blacklistedFromBot;
        this.marriagePartner = marriagePartner;
        this.balance = balance;
        this.reputation = reputation;
        this.dailyStreak = dailyStreak;
        this.lastCrateGiven = lastCrateGiven;
        this.description = description;
        this.lastDailyAt = lastDailyAt;
        this.dustLevel = dustLevel;
        this.lockedUntil = lockedUntil;
        this.activeBadge = activeBadge;
        this.waifuState = waifuState;
        this.actionsDisabled = actionsDisabled;
        this.miningExperience = miningExperience;
        this.fishingExperience = fishingExperience;
        this.chopExperience = chopExperience;
        this.timesMopped = timesMopped;
        this.cratesOpened = cratesOpened;
        this.sharksCaught = sharksCaught;
        this.gamesWon = gamesWon;
        this.marketUsed = marketUsed;
        this.remindedTimes = remindedTimes;
        this.gambleWins = gambleWins;
        this.slotsWins = slotsWins;
        this.gambleWinAmount = gambleWinAmount;
        this.slotsWinAmount = slotsWinAmount;
        this.craftedItems = craftedItems;
        this.repairedItems = repairedItems;
        this.salvagedItems = salvagedItems;
        this.toolsBroken = toolsBroken;
        this.looted = looted;
        this.mined = mined;
        this.gambleLose = gambleLose;
        this.slotsLose = slotsLose;
        this.waifuSlots = waifuSlots;
        this.petSlots = petSlots;
        this.newPlayerNotice = newPlayerNotice;
        this.inventorySortApproach = inventorySortApproach;
        this.birthday = birthday;
        this.timeZone = timeZone;
        this.locale = locale;
    }

    public NewUser(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBlacklistedFromBot() {
        return blacklistedFromBot;
    }

    public void setBlacklistedFromBot(boolean blacklistedFromBot) {
        this.blacklistedFromBot = blacklistedFromBot;
    }

    public NewUser getMarriagePartner() {
        return marriagePartner;
    }

    public void setMarriagePartner(NewUser marriagePartner) {
        this.marriagePartner = marriagePartner;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getDailyStreak() {
        return dailyStreak;
    }

    public void setDailyStreak(int dailyStreak) {
        this.dailyStreak = dailyStreak;
    }

    public Item getLastCrateGiven() {
        return lastCrateGiven;
    }

    public void setLastCrateGiven(Item lastCrateGiven) {
        this.lastCrateGiven = lastCrateGiven;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getLastDailyAt() {
        return lastDailyAt;
    }

    public void setLastDailyAt(Instant lastDailyAt) {
        this.lastDailyAt = lastDailyAt;
    }

    public int getDustLevel() {
        return dustLevel;
    }

    public void setDustLevel(int dustLevel) {
        this.dustLevel = dustLevel;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Instant lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public Badge getActiveBadge() {
        return activeBadge;
    }

    public void setActiveBadge(Badge activeBadge) {
        this.activeBadge = activeBadge;
    }

    public WaifuState getWaifuState() {
        return waifuState;
    }

    public void setWaifuState(WaifuState waifuState) {
        this.waifuState = waifuState;
    }

    public boolean isActionsDisabled() {
        return actionsDisabled;
    }

    public void setActionsDisabled(boolean actionsDisabled) {
        this.actionsDisabled = actionsDisabled;
    }

    public long getMiningExperience() {
        return miningExperience;
    }

    public void setMiningExperience(long miningExperience) {
        this.miningExperience = miningExperience;
    }

    public long getFishingExperience() {
        return fishingExperience;
    }

    public void setFishingExperience(long fishingExperience) {
        this.fishingExperience = fishingExperience;
    }

    public long getChopExperience() {
        return chopExperience;
    }

    public void setChopExperience(long chopExperience) {
        this.chopExperience = chopExperience;
    }

    public int getTimesMopped() {
        return timesMopped;
    }

    public void setTimesMopped(int timesMopped) {
        this.timesMopped = timesMopped;
    }

    public int getCratesOpened() {
        return cratesOpened;
    }

    public void setCratesOpened(int cratesOpened) {
        this.cratesOpened = cratesOpened;
    }

    public int getSharksCaught() {
        return sharksCaught;
    }

    public void setSharksCaught(int sharksCaught) {
        this.sharksCaught = sharksCaught;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getMarketUsed() {
        return marketUsed;
    }

    public void setMarketUsed(int marketUsed) {
        this.marketUsed = marketUsed;
    }

    public int getRemindedTimes() {
        return remindedTimes;
    }

    public void setRemindedTimes(int remindedTimes) {
        this.remindedTimes = remindedTimes;
    }

    public int getGambleWins() {
        return gambleWins;
    }

    public void setGambleWins(int gambleWins) {
        this.gambleWins = gambleWins;
    }

    public int getSlotsWins() {
        return slotsWins;
    }

    public void setSlotsWins(int slotsWins) {
        this.slotsWins = slotsWins;
    }

    public long getGambleWinAmount() {
        return gambleWinAmount;
    }

    public void setGambleWinAmount(long gambleWinAmount) {
        this.gambleWinAmount = gambleWinAmount;
    }

    public long getSlotsWinAmount() {
        return slotsWinAmount;
    }

    public void setSlotsWinAmount(long slotsWinAmount) {
        this.slotsWinAmount = slotsWinAmount;
    }

    public int getCraftedItems() {
        return craftedItems;
    }

    public void setCraftedItems(int craftedItems) {
        this.craftedItems = craftedItems;
    }

    public int getRepairedItems() {
        return repairedItems;
    }

    public void setRepairedItems(int repairedItems) {
        this.repairedItems = repairedItems;
    }

    public int getSalvagedItems() {
        return salvagedItems;
    }

    public void setSalvagedItems(int salvagedItems) {
        this.salvagedItems = salvagedItems;
    }

    public int getToolsBroken() {
        return toolsBroken;
    }

    public void setToolsBroken(int toolsBroken) {
        this.toolsBroken = toolsBroken;
    }

    public int getLooted() {
        return looted;
    }

    public void setLooted(int looted) {
        this.looted = looted;
    }

    public int getMined() {
        return mined;
    }

    public void setMined(int mined) {
        this.mined = mined;
    }

    public long getGambleLose() {
        return gambleLose;
    }

    public void setGambleLose(long gambleLose) {
        this.gambleLose = gambleLose;
    }

    public long getSlotsLose() {
        return slotsLose;
    }

    public void setSlotsLose(long slotsLose) {
        this.slotsLose = slotsLose;
    }

    public int getWaifuSlots() {
        return waifuSlots;
    }

    public void setWaifuSlots(int waifuSlots) {
        this.waifuSlots = waifuSlots;
    }

    public int getPetSlots() {
        return petSlots;
    }

    public void setPetSlots(int petSlots) {
        this.petSlots = petSlots;
    }

    public boolean isNewPlayerNotice() {
        return newPlayerNotice;
    }

    public void setNewPlayerNotice(boolean newPlayerNotice) {
        this.newPlayerNotice = newPlayerNotice;
    }

    public InventorySortType getInventorySortApproach() {
        return inventorySortApproach;
    }

    public void setInventorySortApproach(InventorySortType inventorySortApproach) {
        this.inventorySortApproach = inventorySortApproach;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    /**
     * Removes x amount of money from the player. Only goes though if money removed sums more than zero (avoids negative values).
     *
     * @param toRemove How much?
     */
    public boolean deductBalance(long toRemove) {
        if (balance - toRemove < 0) {
            return false;
        }

        balance -= toRemove;
        return true;
    }
}
