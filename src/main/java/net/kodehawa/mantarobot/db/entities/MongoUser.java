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

package net.kodehawa.mantarobot.db.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.kodehawa.mantarobot.MantaroBot;
import net.kodehawa.mantarobot.commands.currency.item.PlayerEquipment;
import net.kodehawa.mantarobot.data.Config;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.db.ManagedMongoObject;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Reminder: all setters MUST be protected!
@SuppressWarnings("unused")
public class MongoUser implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "users";
    @BsonIgnore
    private final Config config = MantaroData.config().get();
    @BsonIgnore
    public Map<String, Object> fieldTracker = new HashMap<>();

    @BsonId
    private String id;
    private String birthday;
    private int remindedTimes;
    private String timezone;
    private String lang;
    private int dustLevel; //percentage
    private PlayerEquipment equippedItems = new PlayerEquipment(new EnumMap<>(PlayerEquipment.EquipmentType.class), new EnumMap<>(PlayerEquipment.EquipmentType.class), new EnumMap<>(PlayerEquipment.EquipmentType.class)); //hashmap is type -> itemId

    // NEW MARRIAGE SYSTEM
    private String marriageId;

    // user id, value bought for.
    private Map<String, Long> waifus = new HashMap<>();
    private int waifuSlots = 3;
    private int timesClaimed;

    // Persistent reminders. UUID is saved here.
    private List<String> reminders = new ArrayList<>();

    // Hide tag (and ID on waifu) on marriage/waifu list
    private boolean privateTag = false; //just explicitly setting it to false to make sure people know it's the default.
    private boolean autoEquip = false;
    private boolean actionsDisabled = false;

    // Mongo serialization
    public MongoUser() { }

    @SuppressWarnings("SameParameterValue")
    protected MongoUser(String id) {
        this.id = id;
    }

    public static MongoUser of(String id) {
        return new MongoUser(id);
    }

    // --- Getters
    public String getBirthday() {
        return this.birthday;
    }

    public int getRemindedTimes() {
        return this.remindedTimes;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public String getLang() {
        return this.lang;
    }

    public int getDustLevel() {
        return this.dustLevel;
    }

    public PlayerEquipment getEquippedItems() {
        return this.equippedItems;
    }

    public String getMarriageId() {
        return this.marriageId;
    }

    public int getWaifuSlots() {
        return this.waifuSlots;
    }

    public int getTimesClaimed() {
        return this.timesClaimed;
    }

    public boolean isPrivateTag() {
        return this.privateTag;
    }

    public boolean isAutoEquip() {
        return autoEquip;
    }

    public boolean isActionsDisabled() {
        return actionsDisabled;
    }

    // DO NOT INTERACT DIRECTLY WITH, CHANGES TO THE MAP FROM THIS METHOD WILL NOT BE UPDATED
    // Can't make getters protected!
    @BsonProperty("waifus")
    public Map<String, Long> getWaifus() {
        return this.waifus;
    }

    // DO NOT INTERACT DIRECTLY WITH, CHANGES TO THE LIST FROM THIS METHOD WILL NOT BE UPDATED
    // Needed to be public: need to access for non-modifying iterations, making another method would be superfluous.
    public List<String> getReminders() {
        return this.reminders;
    }

    // --- Setters needed for serialization (unless I want to make the structure more rigid and use a constructor)
    protected void setLang(String lang) {
        this.lang = lang;
    }

    protected void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    protected void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    protected void setDustLevel(int dustLevel) {
        this.dustLevel = dustLevel;
    }

    protected void setMarriageId(String marriageId) {
        this.marriageId = marriageId;
    }

    protected void setWaifuSlots(int waifuSlots) {
        this.waifuSlots = waifuSlots;
    }

    protected void setTimesClaimed(int timesClaimed) {
        this.timesClaimed = timesClaimed;
    }

    protected void setPrivateTag(boolean privateTag) {
        this.privateTag = privateTag;
    }

    protected void setAutoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
    }

    protected void setActionsDisabled(boolean actionsDisabled) {
        this.actionsDisabled = actionsDisabled;
    }

    // --- Unused (?) setters, also definitely needed for serialization.
    protected void setWaifus(Map<String, Long> waifus) {
        this.waifus = waifus;
    }

    protected void setReminders(List<String> reminders) {
        this.reminders = reminders;
    }

    public void setRemindedTimes(int remindedTimes) {
        this.remindedTimes = remindedTimes;
    }

    public void setEquippedItems(PlayerEquipment equippedItems) {
        this.equippedItems = equippedItems;
    }


    // --- Track changes to use update
    @BsonIgnore
    public void actionsDisabled(boolean actionsDisabled) {
        this.actionsDisabled = actionsDisabled;
        fieldTracker.put("actionsDisabled", this.actionsDisabled);
    }

    @BsonIgnore
    public void autoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
        fieldTracker.put("autoEquip", this.autoEquip);
    }

    @BsonIgnore
    public void privateTag(boolean privateTag) {
        this.privateTag = privateTag;
        fieldTracker.put("privateTag", this.privateTag);
    }

    @BsonIgnore
    public void waifuSlots(int waifuSlots) {
        this.waifuSlots = waifuSlots;
        fieldTracker.put("waifuSlots", this.waifuSlots);
    }

    @BsonIgnore
    public void marriageId(String marriageId) {
        this.marriageId = marriageId;
        fieldTracker.put("marriageId", this.marriageId);
    }

    @BsonIgnore
    public void dustLevel(int dustLevel) {
        this.dustLevel = dustLevel;
        fieldTracker.put("dustLevel", this.dustLevel);
    }

    @BsonIgnore
    public void timezone(String timezone) {
        this.timezone = timezone;
        fieldTracker.put("timezone", this.timezone);
    }

    @BsonIgnore
    public void language(String lang) {
        this.lang = lang;
        fieldTracker.put("lang", this.lang);
    }

    @BsonIgnore
    public void birthday(String birthday) {
        this.birthday = birthday;
        fieldTracker.put("birthday", this.birthday);
    }

    // Waifu helpers: needed to not interact with the Map directly.
    @BsonIgnore
    public void addWaifu(String id, long value) {
        waifus.put(id, value);
        fieldTracker.put("waifus", this.waifus);
    }

    @BsonIgnore
    public void removeWaifu(String id) {
        waifus.remove(id);
        fieldTracker.put("waifus", this.waifus);
    }

    @BsonIgnore
    public boolean containsWaifu(String id) {
        return waifus.containsKey(id);
    }

    @BsonIgnore
    public long waifuAmount() {
        return waifus.size();
    }

    @BsonIgnore
    public Long getWaifu(String id) {
        return waifus.get(id);
    }

    @BsonIgnore
    public Set<String> waifuKeys() {
        return waifus.keySet();
    }

    @BsonIgnore
    public Set<Map.Entry<String, Long>> waifuEntrySet() {
        return waifus.entrySet();
    }

    @BsonIgnore
    public void addReminder(String reminder) {
        reminders.add(reminder);
        fieldTracker.put("reminders", this.reminders);
    }

    @BsonIgnore
    public void removeReminder(String reminder) {
        reminders.remove(reminder);
        fieldTracker.put("reminders", this.reminders);
    }

    @BsonIgnore
    public User getUser(JDA jda) {
        return jda.retrieveUserById(getId()).complete();
    }

    @BsonIgnore
    public User getUser() {
        return MantaroBot.getInstance().getShardManager().retrieveUserById(getId()).complete();
    }

    @BsonIgnore
    public Marriage getMarriage() {
        //we're going full round trip here
        return MantaroData.db().getMarriage(marriageId);
    }

    @BsonIgnore
    public int increaseDustLevel(int by) {
        int increased = dustLevel + Math.min(1, by);
        if (increased >= 100) {
            this.setDustLevel(100);
            return dustLevel; //same as before, cap at 100.
        }

        this.setDustLevel(increased);
        fieldTracker.put("dustLevel", this.dustLevel);
        return this.dustLevel;
    }

    @BsonIgnore
    public void incrementReminders() {
        remindedTimes += 1;
        fieldTracker.put("remindedTimes", this.remindedTimes);
    }

    @BsonIgnore
    public void incrementTimesClaimed() {
        timesClaimed += 1;
        fieldTracker.put("timesClaimed", this.timesClaimed);
    }

    @Override
    @Nonnull
    public String getId() {
        return this.id;
    }

    @BsonIgnore
    @Override
    @Nonnull
    public String getTableName() {
        return DB_TABLE;
    }

    @BsonIgnore
    @Override
    public void updateAllChanged() {
        MantaroData.db().updateFieldValues(this, fieldTracker);
    }

    @Override
    public void insertOrReplace() {
        MantaroData.db().saveMongo(this, MongoUser.class);
    }

    @Override
    public void delete() {
        MantaroData.db().deleteMongo(this, MongoUser.class);
    }

    public Config getConfig() {
        return this.config;
    }
}
