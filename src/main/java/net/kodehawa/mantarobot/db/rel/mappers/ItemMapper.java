package net.kodehawa.mantarobot.db.rel.mappers;

import net.kodehawa.mantarobot.commands.currency.item.Item;
import net.kodehawa.mantarobot.commands.currency.item.ItemHelper;
import net.kodehawa.mantarobot.commands.currency.item.ItemStack;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements ColumnMapper<Item> {
    @Override
    public Item map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        return ItemHelper.fromId(r.getInt(columnNumber));
    }
}
