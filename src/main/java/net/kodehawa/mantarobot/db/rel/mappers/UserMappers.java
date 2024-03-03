package net.kodehawa.mantarobot.db.rel.mappers;

import net.kodehawa.mantarobot.db.rel.NewUser;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMappers {

    public static class ShallowColumnMapper implements ColumnMapper<NewUser> {
        @Override
        public NewUser map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
            long userId = rs.getLong(columnNumber);
            NewUser r = new NewUser();
            r.setId(userId);
            return r;
        }
    }
}
