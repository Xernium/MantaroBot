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
import net.kodehawa.mantarobot.utils.APIUtils;
import net.kodehawa.mantarobot.utils.Pair;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public class PremiumKey implements ManagedObject {
    private long duration;
    private boolean enabled;
    private long expiration;
    private UUID id;
    private ISnowflake owner;
    private int type;

    private ISnowflake linkedTo;

    public PremiumKey(UUID id, long duration, long expiration, Type type, boolean enabled, ISnowflake owner, ISnowflake linkedTo) {
        this.id = id;
        this.duration = duration;
        this.expiration = expiration;
        this.type = type.ordinal();
        this.enabled = enabled;
        this.owner = owner;
        this.linkedTo = linkedTo;
    }

    public ISnowflake getLinkedTo() {
        return this.linkedTo;
    }

    public void setLinkedTo(ISnowflake linkedTo) {
        this.linkedTo = linkedTo;
    }

    public PremiumKey() {
    }

    public static PremiumKey generatePremiumKey(ISnowflake owner, Type type, boolean linked) {
        UUID premiumId = UUID.randomUUID();
        PremiumKey newKey = new PremiumKey(premiumId, -1, -1, type, false, owner, linked ? owner : null);

        newKey.save();
        return newKey;
    }

    public static PremiumKey generatePremiumKeyTimed(ISnowflake owner, Type type, int days, boolean linked) {
        UUID premiumId = UUID.randomUUID();
        PremiumKey newKey = new PremiumKey(premiumId, TimeUnit.DAYS.toMillis(days), currentTimeMillis() + TimeUnit.DAYS.toMillis(days), type, false, owner, linked ? owner : null);

        newKey.save();
        return newKey;
    }

    public Type getParsedType() {
        return Type.values()[type];
    }

    public long getDurationDays() {
        return TimeUnit.MILLISECONDS.toDays(duration);
    }

    public long validFor() {
        return TimeUnit.MILLISECONDS.toDays(getExpiration() - currentTimeMillis());
    }

    public long validForMs() {
        return getExpiration() - currentTimeMillis();
    }

    public void activate(int days) {
        this.enabled = true;
        this.duration = TimeUnit.DAYS.toMillis(days);
        this.expiration = currentTimeMillis() + TimeUnit.DAYS.toMillis(days);
        save();
    }

    public boolean renew() {
        if (linkedTo != null) {
            Pair<Boolean, String> pledgeInfo = APIUtils.getPledgeInformation(linkedTo);
            if (pledgeInfo != null && pledgeInfo.getLeft()) {
                switch (type) {
                    //user
                    case 1 -> this.activate(365);
                    //server
                    case 2 -> this.activate(180);
                    default -> this.activate(60);
                }

                return true;
            }
        }

        return false;
    }

    public long getDuration() {
        return this.duration;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public long getExpiration() {
        return this.expiration;
    }

    @Override
    public long getIdLong() {
        return id.getMostSignificantBits();
    }

    @Override
    @Nonnull
    public String getTableName() {
        return "PremiumKeys";
    }

    public ISnowflake getOwner() {
        return this.owner;
    }

    public int getType() {
        return this.type;
    }

    public enum Type {
        MASTER, USER, GUILD
    }
}
