package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.UserProfileComponent;
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

public interface UserProfileComponentDao extends DataDao<UserProfileComponent>{

    @SqlQuery("SELECT owner, component, slot FROM UserProfileComponent WHERE owner = :id")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.ProfileComponentMapper.class)
    @RegisterBeanMapper(UserProfileComponent.class)
    Set<UserProfileComponent> getUserProfileComponentsShallow(@BindBean NewUser owner);

    default Set<UserProfileComponent> getUserProfileComponents(NewUser owner) {
        return  getUserProfileComponentsShallow(owner)
                .stream()
                .peek(ele -> ele.setOwner(owner))
                .collect(Collectors.toSet());
    }

    @SqlQuery("SELECT owner, component, slot FROM UserProfileComponent WHERE owner = :id AND slot = :slot")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.ProfileComponentMapper.class)
    @RegisterBeanMapper(UserProfileComponent.class)
    UserProfileComponent getUserProfileComponentShallow(@BindBean NewUser owner, @Bind("slot") int slot);

    default UserProfileComponent getUserProfileComponent(NewUser owner, int slot) {
        return  getUserProfileComponentShallow(owner, slot);
    }

    @SqlQuery("DELETE FROM UserProfileComponent WHERE owner = :id AND slot = :slot")
    void delete(@BindBean NewUser owner, @Bind("slot") int slot);

    @Override
    default void delete(UserProfileComponent del) {
        delete(del.getOwner(), del.getSlot());
    }

    @SqlUpdate("UPDATE UserProfileComponent SET component = :component WHERE owner = :id AND slot = :slot")
    void updateFull(@BindBean NewUser owner, @Bind("slot") int slot, @Bind("component") int component);

     @Override
     default void updateFull(UserProfileComponent upd) {
         updateFull(upd.getOwner(), upd.getSlot(), upd.getWhich().ordinal());
     }

    @SqlUpdate("INSERT INTO UserProfileComponent (owner, component, slot) VALUES (:id, :component, :slot)")
    void insert(@BindBean NewUser owner, @Bind("slot") int slot, @Bind("component") int component);

    @Override
    default void insert(UserProfileComponent ins) {
        updateFull(ins.getOwner(), ins.getSlot(), ins.getWhich().ordinal());
    }

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS UserProfileComponent " +
                    "(" +
                    " owner    BIGINT   NOT NULL," +
                    " component   INT  NOT NULL," +
                    " slot    INTEGER  NOT NULL," +
                    " PRIMARY KEY(owner, slot)," +
                    " FOREIGN KEY(owner) REFERENCES UserData(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    default void createTable() {}

}
