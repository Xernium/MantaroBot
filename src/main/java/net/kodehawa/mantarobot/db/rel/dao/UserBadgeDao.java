package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.commands.currency.profile.Badge;
import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.UserBadge;
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

public interface UserBadgeDao extends DataDao<UserBadge>{

    @SqlQuery("SELECT owner, badge, achievedAt FROM UserBadge WHERE owner = :id")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.BadgeMapper.class)
    @RegisterBeanMapper(UserBadge.class)
    Set<UserBadge> getUsersBadgesShallow(@BindBean NewUser owner);

    default Set<UserBadge> getUsersBadges(NewUser owner) {
        return getUsersBadgesShallow(owner)
                .stream()
                .peek(ele -> ele.setOwner(owner))
                .collect(Collectors.toSet());
    }

    @SqlQuery("SELECT COUNT(owner) FROM UserBadge WHERE owner = :id")
    int getUsersBadgesCountByOwner(@BindBean NewUser owner);

    @SqlQuery("SELECT owner, badge, achievedAt FROM UserBadge WHERE owner = :id AND badge = :badge")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.BadgeMapper.class)
    @RegisterBeanMapper(UserBadge.class)
    UserBadge getUsersBadgeShallow(@BindBean NewUser owner, @Bind("badge") int badge);

    default UserBadge getUsersBadge(NewUser owner, Badge badge) {
        return getUsersBadgeShallow(owner, badge.ordinal());
    }

    @SqlQuery("DELETE FROM UserBadge WHERE owner = :id AND badge = :badge")
    void delete(@BindBean NewUser owner, @Bind("badge") int badge);

    @Override
    default void delete(UserBadge del) {
        delete(del.getOwner(), del.getBadge().ordinal());
    }

    @SqlUpdate("UPDATE UserBadge SET achievedAt = :achievedAt WHERE owner = :id AND badge = :badge")
    void updateFull(@BindBean NewUser owner, @Bind("badge") int badge, @BindBean UserBadge upd);

     @Override
     default void updateFull(UserBadge upd) {
         updateFull(upd.getOwner(), upd.getBadge().ordinal(), upd);
     }

    @SqlUpdate("INSERT INTO UserBadge (owner, badge, achievedAt) VALUES (:id, :badge, :achievedAt)")
    void insert(@BindBean NewUser owner, @Bind("badge") int badge, @BindBean UserBadge upd);

    @Override
    default void insert(UserBadge ins) {
        updateFull(ins.getOwner(), ins.getBadge().ordinal(), ins);
    }

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS UserBadge " +
                    "(" +
                    " owner    BIGINT   NOT NULL," +
                    " badge    INT  NOT NULL," +
                    " achievedAt   BIGINT  NOT NULL," +
                    " PRIMARY KEY(owner, badge)," +
                    " FOREIGN KEY(owner) REFERENCES UserData(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    default void createTable() {}

}
