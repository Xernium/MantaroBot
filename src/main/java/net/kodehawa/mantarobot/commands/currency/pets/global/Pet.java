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
package net.kodehawa.mantarobot.commands.currency.pets.global;

import net.kodehawa.mantarobot.commands.currency.item.PlayerEquipment;
import net.kodehawa.mantarobot.commands.currency.item.PotionEffect;
import net.kodehawa.mantarobot.db.entities.helpers.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static net.kodehawa.mantarobot.db.entities.helpers.Inventory.Resolver.unserialize;

public class Pet {

    private final transient Inventory petInventory = new Inventory();
    private String owner;
    private ImageType image; //Choose from different pre-picked images. Configurable
    private String name; //Only letters.
    private Type element;
    private long epochCreatedAt;
    private long age;
    private long tier; //Calculated between 1 to 20 according to current pet stats.
    private long tradePrice; //Calculated using stats + tier.
   
   public Pet(String owner, String name,Type element, long age, Map<Integer, Integer> inventory) {
        this.owner = owner;
        this.name = name;
        this.element = element;
        this.age = age;
        this.petInventory.replaceWith(unserialize(inventory));
    }

    public static Pet create(String owner, String name, Type element) {
        Pet pet = new Pet(owner, name, element, 1, new HashMap<>());
        pet.setEpochCreatedAt(System.currentTimeMillis());
        return pet;
    }

    public Pet changeImage(ImageType type) {
        this.image = type;
        return this;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ImageType getImage() {
        return this.image;
    }

    public void setImage(ImageType image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getElement() {
        return this.element;
    }

    public void setElement(Type element) {
        this.element = element;
    }

    public long getEpochCreatedAt() {
        return this.epochCreatedAt;
    }

    public void setEpochCreatedAt(long epochCreatedAt) {
        this.epochCreatedAt = epochCreatedAt;
    }

    public long getTier() {
        return this.tier;
    }

    public void setTier(long tier) {
        this.tier = tier;
    }

    public long getTradePrice() {
        return this.tradePrice;
    }

    public void setTradePrice(long tradePrice) {
        this.tradePrice = tradePrice;
    }
    public Inventory getPetInventory() {
        return this.petInventory;
    }

    public long getAge() {
        return System.currentTimeMillis() - getEpochCreatedAt();
    }

    public void setAge(long age) {
        this.age = age;
    }

    
    public long getAgeDays() {
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - getEpochCreatedAt());
    }

    public enum ImageType {
        //hello sukeban studios (https://va11halla.fandom.com/wiki/Lilim)
        SPACESHIP(""), CAT(""), DOG(""), ROBOT(""), LILIM(""), CATGIRL("");

        public final String image;

        ImageType(String image) {
            this.image = image;
        }

        public String getImage() {
            return this.image;
        }
    }

    public enum Type {
        EARTH("Earth", "commands.pet.types.earth"), WATER("Water", "commands.pet.types.water"), FIRE("Fire", "commands.pet.types.fire");

        final String readable;
        final String translatable;

        Type(String readable, String translatable) {
            this.readable = readable;
            this.translatable = translatable;
        }

        public String getReadable() {
            return this.readable;
        }

        public String getTranslatable() {
            return this.translatable;
        }
    }

    //Current pet upgrade level
    public Level upgradeLevel = Level.BASIC; //The bigger this number, the easier it is to gain XP.
    //lol
    private String test;
    private String id; //Why isn't this on the main class?
    private long xp; //Increased through collecting, training and battles.
    private long level; //Same as above.
    private double affection; //Increases randomly with actions that involve "loving" or taking care of your pet.
    private long affectionLevel;
    private long timesPetted;
    private long timesCollected;
    //How many battles
    private long battles;
    //To calculate win/lose ratio
    private long battlesWon;
    private long battlesLost;
    private long battlesDraw;
    //Skills learned -> Skill XP
    private Map<PetSkill, AtomicLong> petSkills = new HashMap<>();
    //Hydration (water type)
    private long hydrationLevel;
    private long lastHydratedAt; //to handle decreasing
    //Collect stats
    private Map<Long, AtomicLong> collected = new HashMap<>();
    private long collectRate;
    private long lastCollectedAt; //to handle increasing
    //Hunger (every type except fire)
    private long hunger;
    private float saturation;
    private long lastFedAt;
    //not so much of "player" anymore
    private PlayerEquipment equippedItems = new PlayerEquipment(new HashSet<>(), new HashSet<>()); //hashmap is type -> itemId

    
    public long getCurrentHydration() {
        long hoursSince = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - lastHydratedAt);
        if (hoursSince > 3) {
            hydrationLevel = Math.min(10, hydrationLevel * hoursSince / 2);
        }
        return hydrationLevel;
    }

    
    public long increaseHydration() {
        hydrationLevel += 10;

        if (hydrationLevel > 100) {
            hydrationLevel = 100;
            return 100;
        }

        return hydrationLevel;
    }

    
    public long increaseHunger(long by) {
        hunger += by;
        if (hunger > 100) {
            hunger = 100;
            return 100;
        }

        return hunger;
    }

    
    //The calculations used to decrease hunger use a negative factor on saturation: the lower, the better.
    public float increaseSaturation(float by) {
        saturation -= by;

        if (saturation < 1) {
            saturation = 1;
            return 1;
        }

        return saturation;
    }

    
    public float updateSaturation() {
        long hoursSince = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - lastFedAt);
        if (hoursSince > 5) {
            saturation = Math.min(10, saturation * hoursSince / 3);
        }
        return saturation;
    }

    
    public long checkCurrentHunger() {
        //very scientific formula
        if (saturation < 3 && TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - lastFedAt) < 10) {
            return hunger;
        }

        long reduction = (long) (Math.max(1, TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() / lastFedAt)) / -saturation);

        return hunger - reduction;
    }

    public String getTest() {
        return this.test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getXp() {
        return this.xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public long getLevel() {
        return this.level;
    }

    public void setLevel(long level) {
        this.level = level;
    }


    public void setAffection(double affection) {
        this.affection = affection;
    }

    public long getAffectionLevel() {
        return this.affectionLevel;
    }

    public void setAffectionLevel(long affectionLevel) {
        this.affectionLevel = affectionLevel;
    }

    public long getTimesPetted() {
        return this.timesPetted;
    }

    public void setTimesPetted(long timesPetted) {
        this.timesPetted = timesPetted;
    }

    public long getTimesCollected() {
        return this.timesCollected;
    }

    public void setTimesCollected(long timesCollected) {
        this.timesCollected = timesCollected;
    }

    public long getBattles() {
        return this.battles;
    }

    public void setBattles(long battles) {
        this.battles = battles;
    }

    public long getBattlesWon() {
        return this.battlesWon;
    }

    public void setBattlesWon(long battlesWon) {
        this.battlesWon = battlesWon;
    }

    public long getBattlesLost() {
        return this.battlesLost;
    }

    public void setBattlesLost(long battlesLost) {
        this.battlesLost = battlesLost;
    }

    public long getBattlesDraw() {
        return this.battlesDraw;
    }

    public void setBattlesDraw(long battlesDraw) {
        this.battlesDraw = battlesDraw;
    }

    public Map<PetSkill, AtomicLong> getPetSkills() {
        return this.petSkills;
    }

    public void setPetSkills(Map<PetSkill, AtomicLong> petSkills) {
        this.petSkills = petSkills;
    }

    public long getHydrationLevel() {
        return this.hydrationLevel;
    }

    public void setHydrationLevel(long hydrationLevel) {
        this.hydrationLevel = hydrationLevel;
    }

    public long getLastHydratedAt() {
        return this.lastHydratedAt;
    }

    public void setLastHydratedAt(long lastHydratedAt) {
        this.lastHydratedAt = lastHydratedAt;
    }

    public Map<Long, AtomicLong> getCollected() {
        return this.collected;
    }

    public void setCollected(Map<Long, AtomicLong> collected) {
        this.collected = collected;
    }

    public long getCollectRate() {
        return this.collectRate;
    }

    public void setCollectRate(long collectRate) {
        this.collectRate = collectRate;
    }

    public long getLastCollectedAt() {
        return this.lastCollectedAt;
    }

    public void setLastCollectedAt(long lastCollectedAt) {
        this.lastCollectedAt = lastCollectedAt;
    }

    public long getHunger() {
        return this.hunger;
    }

    public void setHunger(long hunger) {
        this.hunger = hunger;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public long getLastFedAt() {
        return this.lastFedAt;
    }

    public void setLastFedAt(long lastFedAt) {
        this.lastFedAt = lastFedAt;
    }

    public PlayerEquipment getEquippedItems() {
        return this.equippedItems;
    }

    public void setEquippedItems(PlayerEquipment equippedItems) {
        this.equippedItems = equippedItems;
    }

    public Level getUpgradeLevel() {
        return this.upgradeLevel;
    }

    public void setUpgradeLevel(Level upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public enum PetSkill {
        FISH, MINE, COLLECT, FIGHT;

        
        static final Random random = new Random();

        public static PetSkill getRandom() {
            int x = random.nextInt(PetSkill.values().length);
            return PetSkill.values()[x];
        }
    }

    public enum Level {
        BASIC("", 0), NORMAL("", 5), ADVANCED("", 20), LEGENDARY("", 50);

        final String recipe;
        final long levelRequired;

        Level(String recipe, long level) {
            this.recipe = recipe;
            this.levelRequired = level;
        }
    }

    private boolean inBattle = false;

    //Global statistics
    private long stamina;
    private long hp;
    private boolean fly;
    private boolean venom;

    //Idle buffs
    private double idleRecoveryCoef = 0.1;
    private double idleStaminaRecoveryCoef = 0.2;

    //Battle buffs
    private double battleRecoveryCoef = 0.18;
    private double battleStaminaRecoveryCoef = 0.25;

    //Idle and battle multipliers
    private double recoveryMult = 1.1;
    private double staminaRecoveryMult = 1.14;

    //Current battle stats
    private long currentStamina = getStamina(); //Unless this changes on battle, should remain equal.
    private long currentHP = getHp(); //Unless this changes in battle, should remain equal.
    private boolean elementAffinity;
    private boolean elementBoost;
    private boolean elementQualification;

    //Current recovery stats
    private PotionEffect currentEffect;
    private long regenStat;
    private long staminaRegenCoef;

    private long epochLastBattle;
    private long epochLastIdle;

    public boolean isInBattle() {
        return this.inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    public long getStamina() {
        return this.stamina;
    }

    public void setStamina(long stamina) {
        this.stamina = stamina;
    }

    public long getHp() {
        return this.hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public boolean isFly() {
        return this.fly;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public boolean isVenom() {
        return this.venom;
    }

    public void setVenom(boolean venom) {
        this.venom = venom;
    }

    public void setAffection(long affection) {
        this.affection = affection;
    }

    public double getIdleRecoveryCoef() {
        return this.idleRecoveryCoef;
    }

    public void setIdleRecoveryCoef(double idleRecoveryCoef) {
        this.idleRecoveryCoef = idleRecoveryCoef;
    }

    public double getIdleStaminaRecoveryCoef() {
        return this.idleStaminaRecoveryCoef;
    }

    public void setIdleStaminaRecoveryCoef(double idleStaminaRecoveryCoef) {
        this.idleStaminaRecoveryCoef = idleStaminaRecoveryCoef;
    }

    public double getBattleRecoveryCoef() {
        return this.battleRecoveryCoef;
    }

    public void setBattleRecoveryCoef(double battleRecoveryCoef) {
        this.battleRecoveryCoef = battleRecoveryCoef;
    }

    public double getBattleStaminaRecoveryCoef() {
        return this.battleStaminaRecoveryCoef;
    }

    public void setBattleStaminaRecoveryCoef(double battleStaminaRecoveryCoef) {
        this.battleStaminaRecoveryCoef = battleStaminaRecoveryCoef;
    }

    public double getRecoveryMult() {
        return this.recoveryMult;
    }

    public void setRecoveryMult(double recoveryMult) {
        this.recoveryMult = recoveryMult;
    }

    public double getStaminaRecoveryMult() {
        return this.staminaRecoveryMult;
    }

    public void setStaminaRecoveryMult(double staminaRecoveryMult) {
        this.staminaRecoveryMult = staminaRecoveryMult;
    }

    public long getCurrentStamina() {
        return this.currentStamina;
    }

    public void setCurrentStamina(long currentStamina) {
        this.currentStamina = currentStamina;
    }

    public long getCurrentHP() {
        return this.currentHP;
    }

    public void setCurrentHP(long currentHP) {
        this.currentHP = currentHP;
    }

    public boolean isElementAffinity() {
        return this.elementAffinity;
    }

    public void setElementAffinity(boolean elementAffinity) {
        this.elementAffinity = elementAffinity;
    }

    public boolean isElementBoost() {
        return this.elementBoost;
    }

    public void setElementBoost(boolean elementBoost) {
        this.elementBoost = elementBoost;
    }

    public boolean isElementQualification() {
        return this.elementQualification;
    }

    public void setElementQualification(boolean elementQualification) {
        this.elementQualification = elementQualification;
    }

    public PotionEffect getCurrentEffect() {
        return this.currentEffect;
    }

    public void setCurrentEffect(PotionEffect currentEffect) {
        this.currentEffect = currentEffect;
    }

    public long getRegenStat() {
        return this.regenStat;
    }

    public void setRegenStat(long regenStat) {
        this.regenStat = regenStat;
    }

    public long getStaminaRegenCoef() {
        return this.staminaRegenCoef;
    }

    public void setStaminaRegenCoef(long staminaRegenCoef) {
        this.staminaRegenCoef = staminaRegenCoef;
    }

    public long getEpochLastBattle() {
        return this.epochLastBattle;
    }

    public void setEpochLastBattle(long epochLastBattle) {
        this.epochLastBattle = epochLastBattle;
    }

    public long getEpochLastIdle() {
        return this.epochLastIdle;
    }

    public void setEpochLastIdle(long epochLastIdle) {
        this.epochLastIdle = epochLastIdle;
    }
}
