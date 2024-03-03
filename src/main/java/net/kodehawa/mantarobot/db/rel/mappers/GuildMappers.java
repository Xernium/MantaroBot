package net.kodehawa.mantarobot.db.rel.mappers;

import net.kodehawa.mantarobot.db.rel.NewGuild;
import net.kodehawa.mantarobot.db.rel.NewUser;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GuildMappers {

    public static class ShallowColumnMapper implements ColumnMapper<NewGuild> {
        @Override
        public NewGuild map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
            long guildId = rs.getLong(columnNumber);
            NewGuild r = new NewGuild();
            r.setId(guildId);
            return r;
        }
    }
}
