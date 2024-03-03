package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.commands.currency.item.ItemHelper;
import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.UserProfileComponent;
import net.kodehawa.mantarobot.db.rel.mappers.EnumTypeMappers;
import net.kodehawa.mantarobot.db.rel.mappers.ItemMapper;
import net.kodehawa.mantarobot.db.rel.mappers.NativeMappers;
import net.kodehawa.mantarobot.db.rel.mappers.UserMappers;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Collectors;

// I hate this class with every fiber of my being
public interface NewUserDao extends DataDao<NewUser>{

    @SqlQuery("SELECT * FROM UserData WHERE id = :id")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(ItemMapper.class)
    @RegisterColumnMapper(NativeMappers.InstantMapper.class)
    @RegisterColumnMapper(NativeMappers.LocaleMapper.class)
    @RegisterColumnMapper(NativeMappers.TimeZoneMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.BadgeMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.WaifuStateMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.InventorySortTypeMapper.class)
    @RegisterBeanMapper(NewUser.class)
    NewUser getUserById(@Bind("id") long id);

    @Override
    @SqlQuery("DELETE FROM UserData WHERE id = :id")
    void delete(@BindBean NewUser del);

    @SqlUpdate("UPDATE UserData " +
            "SET " +
            "    blacklistedFromBot = :blacklistedFromBot," +
            "    marriagePartner = :marriagePartner," +
            "    balance = :balance," +
            "    reputation = :reputation," +
            "    dailyStreak = :dailyStreak," +
            "    lastCrateGiven = :lastCrateGiven," +
            "    description = :description," +
            "    lastDailyAt = :lastDailyAt," +
            "    dustLevel = :dustLevel," +
            "    lockedUntil = :lockedUntil," +
            "    activeBadge = :activeBadge," +
            "    waifuState = :waifuState," +
            "    actionsDisabled = :actionsDisabled," +
            "    miningExperience = :miningExperience," +
            "    fishingExperience = :fishingExperience," +
            "    chopExperience = :chopExperience," +
            "    timesMopped = :timesMopped," +
            "    cratesOpened = :cratesOpened," +
            "    sharksCaught = :sharksCaught," +
            "    gamesWon = :gamesWon," +
            "    marketUsed = :marketUsed," +
            "    remindedTimes = :remindedTimes," +
            "    gambleWins = :gambleWins," +
            "    slotsWins = :slotsWins," +
            "    gambleWinAmount = :gambleWinAmount," +
            "    slotsWinAmount = :slotsWinAmount," +
            "    craftedItems = :craftedItems," +
            "    repairedItems = :repairedItems," +
            "    salvagedItems = :salvagedItems," +
            "    toolsBroken = :toolsBroken," +
            "    looted = :looted," +
            "    mined = :mined," +
            "    gambleLose = :gambleLose," +
            "    slotsLose = :slotsLose," +
            "    waifuSlots = :waifuSlots," +
            "    petSlots = :petSlots," +
            "    newPlayerNotice = :newPlayerNotice," +
            "    inventorySortApproach = :inventorySortApproach," +
            "    birthday = :birthday," +
            "    timeZone = :timeZone," +
            "    locale = :locale" +
            "WHERE id = :id;")
    void updateFull(@BindBean NewUser upd,
                    @Bind("marriagePartner") Long marriagePartner,
                    @Bind("lastCrateGiven") Integer lastCrateGiven,
                    @Bind("lastDailyAt") long lastDailyAt,
                    @Bind("lockedUntil") Long lockedUntil,
                    @Bind("activeBadge") Integer activeBadge,
                    @Bind("waifuState") Integer waifuState,
                    @Bind("inventorySortApproach") int inventorySortApproach,
                    @Bind("timeZone") String timeZone,
                    @Bind("locale") String locale);

    @Override
    default void updateFull(NewUser upd) {
         updateFull(upd,
                 upd.getMarriagePartner() != null ? upd.getMarriagePartner().getId() : null,
                 upd.getLastCrateGiven() != null ? ItemHelper.idOf(upd.getLastCrateGiven()) : null,
                 upd.getLastDailyAt().toEpochMilli(),
                 upd.getLockedUntil() != null ? upd.getLockedUntil().toEpochMilli() : null,
                 upd.getActiveBadge() != null ? upd.getActiveBadge().ordinal() : null,
                 upd.getWaifuState().ordinal(),
                 upd.getInventorySortApproach().ordinal(),
                 upd.getTimeZone().toZoneId().getId(),
                 upd.getLocale().toLanguageTag());
    }

    @SqlUpdate("INSERT INTO UserData (" +
            "    id," +
            "    blacklistedFromBot," +
            "    marriagePartner," +
            "    balance," +
            "    reputation," +
            "    dailyStreak," +
            "    lastCrateGiven," +
            "    description," +
            "    lastDailyAt," +
            "    dustLevel," +
            "    lockedUntil," +
            "    activeBadge," +
            "    waifuState," +
            "    actionsDisabled," +
            "    miningExperience," +
            "    fishingExperience," +
            "    chopExperience," +
            "    timesMopped," +
            "    cratesOpened," +
            "    sharksCaught," +
            "    gamesWon," +
            "    marketUsed," +
            "    remindedTimes," +
            "    gambleWins," +
            "    slotsWins," +
            "    gambleWinAmount," +
            "    slotsWinAmount," +
            "    craftedItems," +
            "    repairedItems," +
            "    salvagedItems," +
            "    toolsBroken," +
            "    looted," +
            "    mined," +
            "    gambleLose," +
            "    slotsLose," +
            "    waifuSlots," +
            "    petSlots," +
            "    newPlayerNotice," +
            "    inventorySortApproach," +
            "    birthday," +
            "    timeZone," +
            "    locale" +
            ") VALUES (" +
            "    :id," +
            "    :blacklistedFromBot," +
            "    :marriagePartner," +
            "    :balance," +
            "    :reputation," +
            "    :dailyStreak," +
            "    :lastCrateGiven," +
            "    :description," +
            "    :lastDailyAt," +
            "    :dustLevel," +
            "    :lockedUntil," +
            "    :activeBadge," +
            "    :waifuState," +
            "    :actionsDisabled," +
            "    :miningExperience," +
            "    :fishingExperience," +
            "    :chopExperience," +
            "    :timesMopped," +
            "    :cratesOpened," +
            "    :sharksCaught," +
            "    :gamesWon," +
            "    :marketUsed," +
            "    :remindedTimes," +
            "    :gambleWins," +
            "    :slotsWins," +
            "    :gambleWinAmount," +
            "    :slotsWinAmount," +
            "    :craftedItems," +
            "    :repairedItems," +
            "    :salvagedItems," +
            "    :toolsBroken," +
            "    :looted," +
            "    :mined," +
            "    :gambleLose," +
            "    :slotsLose," +
            "    :waifuSlots," +
            "    :petSlots," +
            "    :newPlayerNotice," +
            "    :inventorySortApproach," +
            "    :birthday," +
            "    :timeZone," +
            "    :locale" +
            ");")
    void insert(@BindBean NewUser upd,
                @Bind("marriagePartner") Long marriagePartner,
                @Bind("lastCrateGiven") Integer lastCrateGiven,
                @Bind("lastDailyAt") long lastDailyAt,
                @Bind("lockedUntil") Long lockedUntil,
                @Bind("activeBadge") Integer activeBadge,
                @Bind("waifuState") Integer waifuState,
                @Bind("inventorySortApproach") int inventorySortApproach,
                @Bind("timeZone") String timeZone,
                @Bind("locale") String locale);

    @Override
    default void insert(NewUser ins) {
        insert(ins,
                ins.getMarriagePartner() != null ? ins.getMarriagePartner().getId() : null,
                ins.getLastCrateGiven() != null ? ItemHelper.idOf(ins.getLastCrateGiven()) : null,
                ins.getLastDailyAt().toEpochMilli(),
                ins.getLockedUntil() != null ? ins.getLockedUntil().toEpochMilli() : null,
                ins.getActiveBadge() != null ? ins.getActiveBadge().ordinal() : null,
                ins.getWaifuState().ordinal(),
                ins.getInventorySortApproach().ordinal(),
                ins.getTimeZone().toZoneId().getId(),
                ins.getLocale().toLanguageTag());
    }

    @Override
    @SqlUpdate(
            "CCREATE TABLE IF NOT EXISTS UserData" +
                    "(" +
                    " id     BIGINT   NOT NULL UNIQUE," +
                    " blacklistedFromBot   BOOLEAN  NOT NULL," +
                    " marriagePartner   BIGINT," +
                    " balance    BIGINT  NOT NULL," +
                    " reputation   BIGINT  NOT NULL," +
                    " dailyStreak   INT  NOT NULL," +
                    " lastCrateGiven   INT," +
                    " description    VARCHAR," +
                    " lastDailyAt    BIGINT  NOT NULL," +
                    " dustLevel    INT  NOT NULL," +
                    " lockedUntil    BIGINT," +
                    " activeBadge   INT," +
                    " waifuState   INT  NOT NULL," +
                    " actionsDisabled   BOOLEAN  NOT NULL," +
                    " miningExperience   BIGINT  NOT NULL," +
                    " fishingExperience   BIGINT  NOT NULL," +
                    " chopExperience    BIGINT  NOT NULL," +
                    " timesMopped    INT  NOT NULL," +
                    " cratesOpened    INT  NOT NULL," +
                    " sharksCaught    INT  NOT NULL," +
                    " gamesWon    INT  NOT NULL," +
                    " marketUsed   INT  NOT NULL," +
                    " remindedTimes    INT  NOT NULL," +
                    " gambleWins    INT  NOT NULL," +
                    " slotsWins    INT  NOT NULL," +
                    " gambleWinAmount   BIGINT  NOT NULL," +
                    " slotsWinAmount    BIGINT  NOT NULL," +
                    " craftedItems    INT  NOT NULL," +
                    " repairedItems    INT  NOT NULL," +
                    " salvagedItems    INT  NOT NULL," +
                    " toolsBroken    INT  NOT NULL," +
                    " looted     INT  NOT NULL," +
                    " mined     INT  NOT NULL," +
                    " gambleLose    BIGINT  NOT NULL," +
                    " slotsLose    BIGINT  NOT NULL," +
                    " waifuSlots    INT   NOT NULL," +
                    " petSlots    INT  NOT NULL," +
                    " newPlayerNotice   BOOLEAN  NOT NULL," +
                    " inventorySortApproach   INT  NOT NULL," +
                    " birthday   DATE," +
                    " timeZone    VARCHAR(20)," +
                    " locale     VARCHAR(3)," +
                    " PRIMARY KEY(id)," +
                    " FOREIGN KEY(marriagePartner) REFERENCES UserData(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE SET NULL" +
                    ");")
    default void createTable() {}

}
