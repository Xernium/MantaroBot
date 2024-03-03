package net.kodehawa.mantarobot.db.rel.mappers;

import net.kodehawa.mantarobot.commands.currency.item.ItemStack;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemStackMapper implements RowMapper<ItemStack> {
    @Override
    public ItemStack map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ItemStack(rs.getInt("item"), rs.getInt("amount"));
    }
}
