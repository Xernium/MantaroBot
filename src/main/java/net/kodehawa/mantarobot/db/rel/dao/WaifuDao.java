package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.Waifu;
import net.kodehawa.mantarobot.db.rel.mappers.UserMappers;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Collectors;

public interface WaifuDao extends DataDao<Waifu>{

    @SqlQuery("SELECT owner, valuePaid, claimed FROM Waifu WHERE owner = :owner")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterBeanMapper(Waifu.class)
    Set<Waifu> getWaifusByOwnerShallow(@Bind("owner") long ownerId);

    @SqlQuery("SELECT COUNT(owner) FROM Waifu WHERE owner = :owner")
    int getWaifuCountByOwnerPrimitive(@Bind("owner") long ownerId);

    default int getWaifuCountByOwner(NewUser owner) {
        return getWaifuCountByOwnerPrimitive(owner.getId());
    }

    @SqlQuery("SELECT COUNT(claimed) FROM Waifu WHERE claimed = :claimed")
    int getWaifuCountByClaimedPrimitive(@Bind("claimed") long claimedId);

    default int getWaifuCountByClaimed(NewUser claimed) {
        return getWaifuCountByOwnerPrimitive(claimed.getId());
    }

    default Set<Waifu> getWaifusByOwner(NewUser owner) {
        return getWaifusByOwnerShallow(owner.getId())
                .stream()
                .peek(waifu -> waifu.setOwner(owner))
                .collect(Collectors.toSet());
    }

    @SqlQuery("SELECT owner, valuePaid, claimed FROM Waifu WHERE claimed = :claimed")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterBeanMapper(Waifu.class)
    Set<Waifu> getWaifusByClaimedShallow(@Bind("claimed") long claimedId);

    default Set<Waifu> getWaifusByClaimed(NewUser claimed) {
        return getWaifusByOwnerShallow(claimed.getId());
    }


    @SqlQuery("SELECT owner, valuePaid, claimed FROM Waifu WHERE owner = :owner AND claimed = :claimed")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterBeanMapper(Waifu.class)
    Waifu getWaifuByKeyNative(@Bind("owner") long ownerId, @Bind("claimed") long claimedId);

    default Waifu getWaifuByKey(NewUser owner, NewUser claimed){
        return getWaifuByKeyNative(owner.getId(), claimed.getId());
    }

    @Override
    @SqlQuery("DELETE FROM Waifu WHERE owner = :owner AND claimed = :claimed")
    void delete(@BindBean Waifu del);

    @SqlQuery("DELETE FROM Waifu WHERE claimed = :id")
    void deleteByClaimed(@BindBean NewUser del);

    @Override
    @SqlUpdate("UPDATE Waifu SET valuePaid = :valuePaid WHERE owner = :owner AND claimed = :claimed")
    default void updateFull(@BindBean Waifu upd) {}

    @Override
    @SqlUpdate("INSERT INTO Waifu (owner, valuePaid, claimed) VALUES (:owner, :valuePaid, :claimed)")
    default void insert(Waifu ins) {}

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS Waifu " +
            "(" +
            "    owner         BIGINT NOT NULL," +
            "    valuePaid     BIGINT NOT NULL," +
            "    claimed       BIGINT NOT NULL," +
            "    PRIMARY KEY(owner, claimed)," +
            "    FOREIGN KEY(owner) REFERENCES User(id) MATCH SIMPLE" +
            "        ON UPDATE CASCADE" +
            "        ON DELETE CASCADE," +
            "    FOREIGN KEY(claimed) REFERENCES User(id) MATCH SIMPLE" +
            "        ON UPDATE CASCADE" +
            "        ON DELETE CASCADE" +
            ")")
    default void createTable() {}

}
