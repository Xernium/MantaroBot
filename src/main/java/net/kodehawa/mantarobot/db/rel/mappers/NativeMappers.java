package net.kodehawa.mantarobot.db.rel.mappers;

import net.kodehawa.mantarobot.db.rel.NewUser;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

// ISTG this was made more difficult just to fuck with me
public class NativeMappers {

    public static class InstantMapper implements ColumnMapper<Instant> {
        @Override
        public Instant map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
            return Instant.ofEpochMilli(rs.getLong(columnNumber))
        }
    }

    public static class TimeZoneMapper implements ColumnMapper<TimeZone> {
        @Override
        public TimeZone map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
            return TimeZone.getTimeZone(ZoneId.of(rs.getString(columnNumber)));
        }
    }

    public static class LocaleMapper implements ColumnMapper<Locale> {
        @Override
        public Locale map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
            return Locale.forLanguageTag(rs.getString(columnNumber));
        }
    }
}
