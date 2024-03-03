package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.db.rel.*;
import net.kodehawa.mantarobot.db.rel.mappers.GuildMappers;
import net.kodehawa.mantarobot.db.rel.mappers.UserMappers;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Collectors;

public interface CustomCommandResponseDao extends DataDao<CustomCommandResponse> {

    @SqlQuery("SELECT response FROM CustomCommandResponse WHERE command = :name AND guild = :owner")
    @RegisterBeanMapper(CustomCommandResponse.class)
    Set<CustomCommandResponse> getCommandResponsesUnbound(@BindBean CustomCommand command);

    default Set<CustomCommandResponse> getCustomCommandResponses(CustomCommand command) {
        return getCommandResponsesUnbound(command)
                .stream()
                .peek(element -> element.setCommand(command))
                .collect(Collectors.toSet());
    }

    @SqlUpdate("DELETE FROM CustomCommandResponse WHERE command = :command AND guild = :guild AND response = :response")
    void delete(@Bind("guild") long guild, @Bind("command") String command, @Bind("response") String response);

    @Override
    default void delete(CustomCommandResponse del) {
        delete(del.getCommand().getOwner().getId(), del.getCommand().getName(), del.getResponse());
    }

    @Override
    default void updateFull(CustomCommandResponse upd) {
        throw new IllegalArgumentException("Cannot update a full key indexed table");
    }

    @SqlUpdate("INSERT INTO CustomCommandResponse (command, guild, response) VALUES (:command, :guild, :response)")
    void insert(@Bind("guild") long guild, @Bind("command") String command, @Bind("response") String response);

    @Override
    default void insert(CustomCommandResponse ins) {
        insert(ins.getCommand().getOwner().getId(), ins.getCommand().getName(), ins.getResponse());
    }

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS CustomCommandResponse " +
                    "(" +
                    "     command    VARCHAR  NOT NULL," +
                    "     guild     BIGINT   NOT NULL," +
                    " response   VARCHAR  NOT NULL," +
                    " PRIMARY KEY(command, guild, response)," +
                    " FOREIGN KEY(command, guild) REFERENCES CustomCommand(name, owner) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    void createTable();
}
