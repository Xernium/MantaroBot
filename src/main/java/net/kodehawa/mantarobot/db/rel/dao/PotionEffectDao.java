package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.PotionEffect;
import net.kodehawa.mantarobot.db.rel.Waifu;
import net.kodehawa.mantarobot.db.rel.mappers.EnumTypeMappers;
import net.kodehawa.mantarobot.db.rel.mappers.UserMappers;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Collectors;

public interface PotionEffectDao extends DataDao<PotionEffect>{

    @SqlQuery("SELECT appliedTo, until, type, timesUsed, amountEquipped FROM PotionEffect WHERE appliedTo = :appliedTo")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.PotionTypeMapper.class)
    @RegisterBeanMapper(PotionEffect.class)
    Set<PotionEffect> getPotionEffectsByOwnerShallow(@Bind("appliedTo") long appliedTo);

    default Set<PotionEffect> getPotionEffectsByOwner(NewUser appliedTo) {
        return getPotionEffectsByOwnerShallow(appliedTo.getId())
                .stream()
                .peek(eff -> eff.setAppliedTo(appliedTo))
                .collect(Collectors.toSet());
    }

    @SqlUpdate("DELETE FROM PotionEffect WHERE appliedTo = :appliedTo AND type = :type")
    void delete(@Bind("appliedTo") long appliedTo, @Bind("type") int type);

    @Override
    default void delete(PotionEffect del) {
        delete(del.getAppliedTo().getId(), del.getType().ordinal());
    }


    @SqlUpdate("UPDATE PotionEffect SET until = :until, timesUsed = :timesUsed, amountEquipped = :amountEquipped" +
            " WHERE appliedTo = :appliedTo AND type = :type")
    void updateFull(@Bind("appliedTo") long appliedTo, @Bind("type") int type, @BindBean PotionEffect upd);

    @Override
    default void updateFull(PotionEffect upd) {
        updateFull(upd.getAppliedTo().getId(), upd.getType().ordinal(), upd);
    }


    @SqlUpdate("INSERT INTO PotionEffect (appliedTo, until, type, timesUsed, amountEquipped) " +
            "VALUES (:appliedTo, :until, :type, :timesUsed, :amountEquipped)")
    void insert(@Bind("appliedTo") long appliedTo, @Bind("type") int type, @BindBean PotionEffect upd);

    @Override
    default void insert(PotionEffect ins) {
        insert(ins.getAppliedTo().getId(), ins.getType().ordinal(), ins);
    }

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS PotionEffect " +
                    "(" +
                    " appliedTo   BIGINT  NOT NULL," +
                    " type    INT  NOT NULL," +
                    " until    BIGINT  NOT NULL," +
                    " timesUsed   INT  NOT NULL," +
                    " amountEquipped   INT  NOT NULL," +
                    " PRIMARY KEY(appliedTo, type)," +
                    " FOREIGN KEY(appliedTo) REFERENCES UserData(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    void createTable();

}
