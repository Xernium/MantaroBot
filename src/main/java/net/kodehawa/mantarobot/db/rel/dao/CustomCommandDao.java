package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.db.rel.CustomCommand;
import net.kodehawa.mantarobot.db.rel.NewGuild;
import net.kodehawa.mantarobot.db.rel.mappers.GuildMappers;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Collectors;

public interface CustomCommandDao extends DataDao<CustomCommand> {
    @SqlQuery("SELECT name, owner, nsfw, locked FROM CustomCommand WHERE owner = :owner")
    @RegisterColumnMapper(GuildMappers.ShallowColumnMapper.class)
    @RegisterBeanMapper(CustomCommand.class)
    Set<CustomCommand> getCommandsByGuildShallow(@Bind("owner") long ownerId);

    default Set<CustomCommand> getCommandsByGuild(NewGuild owner) {
        return getCommandsByGuildShallow(owner.getId())
                .stream()
                .peek(cc -> cc.setOwner(owner))
                .collect(Collectors.toSet());
    }

    @SqlQuery("SELECT name, owner, nsfw, locked FROM CustomCommand WHERE owner = :owner AND name = :name")
    @RegisterColumnMapper(GuildMappers.ShallowColumnMapper.class)
    @RegisterBeanMapper(CustomCommand.class)
    CustomCommand getCommandByKeyShallow(@Bind("owner") long ownerId, @Bind("name") String name);

    default CustomCommand getCommandByKey(NewGuild owner, String name) {
        CustomCommand r = getCommandByKeyShallow(owner.getId(), name);
        if (r != null) {
            r.setOwner(owner);
        }
        return r;
    }

    @SqlQuery("DELETE FROM CustomCommand WHERE owner = :owner AND name = :name")
    void delete(@Bind("owner") long owner, @BindBean CustomCommand upd);

    @Override
    default void delete(CustomCommand del) {
        delete(del.getOwner().getId(), del);
    }

    @SqlUpdate("UPDATE CustomCommand SET nsfw = :nsfw, locked = :locked" +
            "WHERE owner = :owner AND name = :name")
    void updateFull(@Bind("owner") long owner, @BindBean CustomCommand upd);

    @Override
    default void updateFull(CustomCommand upd) {
        updateFull(upd.getOwner().getId(), upd);
    }

    @SqlUpdate("INSERT INTO CustomCommand (name, owner, nsfw, locked) " +
            "VALUES (:name, :owner, :nsfw, :locked)")
    void insert(@Bind("owner") long owner, @BindBean CustomCommand upd);

    @Override
    default void insert(CustomCommand ins) {
        insert(ins.getOwner().getId(), ins);
    }

    @Override
    @SqlQuery(
            "CREATE TABLE IF NOT EXISTS CustomCommand " +
                    "(" +
                    " name    VARCHAR  NOT NULL," +
                    " owner    BIGINT  NOT NULL," +
                    " nsfw    BOOLEAN  NOT NULL," +
                    " locked    BOOLEAN  NOT NULL," +
                    " PRIMARY KEY(name, owner)," +
                    " FOREIGN KEY(owner) REFERENCES Guild(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    default void createTable() {}
}
