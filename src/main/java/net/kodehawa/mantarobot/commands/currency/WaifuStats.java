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

package net.kodehawa.mantarobot.commands.currency;

import net.kodehawa.mantarobot.db.rel.NewUser;

// Unused, my ass
@SuppressWarnings("unused")
public class WaifuStats {

    private static final long WAIFU_BASE_VALUE = 1000L;
    private long moneyValue;
    private long badgeValue;
    private double reputationMultiplier;
    private long claimValue;
    private long finalValue;
    private long performance;

    public WaifuStats(NewUser who, int timesClaimed, int badgesCount){

        var waifuValue = WAIFU_BASE_VALUE;
        long performance;
        // For every 135,000 money owned, it increases by 7% base value (base: 1300)
        // For every 3 badges, it increases by 17% base value.
        // For every 2,780 experience, the value increases by 18% of the base value.
        // After all those calculations are complete,
        // the value then is calculated using final * (reputation scale / 10) where reputation scale goes up by 1 every 10 reputation points.
        // For every 3 waifu claims, the final value increases by 5% of the base value.
        // Maximum waifu value is Integer.MAX_VALUE.

        //Money calculation.
        long moneyValue = Math.round(Math.max(1, (int) (who.getBalance() / 135000)) * calculatePercentage(6));
        //Badge calculation.
        long badgeValue = Math.round(Math.max(1, (badgesCount / 3)) * calculatePercentage(17));
        //Claim calculator.
        long claimValue = Math.round(Math.max(1, (timesClaimed / 3)) * calculatePercentage(5));

        //"final" value
        waifuValue += moneyValue + badgeValue + claimValue;

        // what is this lol
        // After all those calculations are complete, the value then is calculated using final *
        // (reputation scale / 20) where reputation scale goes up by 1 every 10 reputation points.
        // At 6,500 reputation points, the waifu value gets multiplied by 1.1. This is the maximum amount it can be multiplied to.
        // to implement later: Reputation scaling is capped at 5k. Then at 6.5k the multiplier is applied.
        var reputation = who.getReputation();
        var reputationScale = reputation;
        if (reputation > 5000) {
            reputationScale = 5000;
        }

        var reputationScaling = (reputationScale / 4.5) / 30;
        var finalValue = (long) (
                Math.min(Integer.MAX_VALUE, (waifuValue * (reputationScaling > 1 ? reputationScaling : 1) * (reputation > 6500 ? 1.1 : 1)))
        );

        var divide = (int) (moneyValue / 1300);
        performance = ((waifuValue - (WAIFU_BASE_VALUE + 450)) + (long) ((reputationScaling > 1 ? reputationScaling : 1) * 1.2)) / (divide > 1 ? divide : 3);

        //possible?
        if (performance < 0) {
            performance = 0;
        }

        this.moneyValue = moneyValue;
        this.reputationMultiplier = reputationScaling;
        this.claimValue = claimValue;
        this.finalValue = finalValue;
        this.performance = performance;
    }

    public WaifuStats(long moneyValue, long badgeValue,
                      double reputationMultiplier, long claimValue, long finalValue, long performance) {
        this.moneyValue = moneyValue;
        this.reputationMultiplier = reputationMultiplier;
        this.claimValue = claimValue;
        this.finalValue = finalValue;
        this.performance = performance;
    }

    public long getMoneyValue() {
        return this.moneyValue;
    }

    public void setMoneyValue(long moneyValue) {
        this.moneyValue = moneyValue;
    }

    public long getBadgeValue() {
        return this.badgeValue;
    }

    public void setBadgeValue(long badgeValue) {
        this.badgeValue = badgeValue;
    }

    public double getReputationMultiplier() {
        return this.reputationMultiplier;
    }

    public void setReputationMultiplier(double reputationMultiplier) {
        this.reputationMultiplier = reputationMultiplier;
    }

    public long getClaimValue() {
        return this.claimValue;
    }

    public void setClaimValue(long claimValue) {
        this.claimValue = claimValue;
    }

    public long getFinalValue() {
        return this.finalValue;
    }

    public void setFinalValue(long finalValue) {
        this.finalValue = finalValue;
    }

    public long getPerformance() {
        return this.performance;
    }

    public void setPerformance(long performance) {
        this.performance = performance;
    }

    private static long calculatePercentage(long percentage) {
        return (percentage * WAIFU_BASE_VALUE) / 100;
    }
}
