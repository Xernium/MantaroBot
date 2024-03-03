package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.commands.currency.item.Item;
import net.kodehawa.mantarobot.commands.currency.item.ItemHelper;
import net.kodehawa.mantarobot.db.rel.InventoryItem;
import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.mappers.ItemStackMapper;
import net.kodehawa.mantarobot.db.rel.mappers.UserMappers;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;
import java.util.stream.Collectors;

public interface InventoryItemDao extends DataDao<InventoryItem>{

    @SqlQuery("SELECT owner, item, amount FROM InventoryItem WHERE owner = :id")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterRowMapper(ItemStackMapper.class)
    @RegisterBeanMapper(InventoryItem.class)
    Set<InventoryItem> getUserInventoryShallow(@BindBean NewUser owner);

    default Set<InventoryItem> getUserInventory(NewUser owner) {
        return getUserInventoryShallow(owner)
                .stream()
                .peek(ele -> ele.setOwner(owner))
                .collect(Collectors.toSet());
    }

    @SqlQuery("SELECT owner, item, amount FROM InventoryItem WHERE owner = :id AND item = :item")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterRowMapper(ItemStackMapper.class)
    @RegisterBeanMapper(InventoryItem.class)
    InventoryItem getUserItemShallow(@BindBean NewUser owner, @Bind("item") int itemId);

    default InventoryItem getUserItem(NewUser owner, Item item) {
        return getUserItemShallow(owner, ItemHelper.idOf(item));
    }


    @SqlQuery("DELETE FROM InventoryItem WHERE owner = :id AND item = :item")
    void delete(@BindBean NewUser owner, @Bind("item") int itemId);

    @Override
    default void delete(InventoryItem del) {
        delete(del.getOwner(), ItemHelper.idOf(del.getItemStack().getItem()));
    }

    @SqlUpdate("UPDATE InventoryItem SET amount = :amount WHERE owner = :id AND item = :item")
    void updateFull(@BindBean NewUser owner, @Bind("item") int itemId, @BindBean InventoryItem upd);

     @Override
     default void updateFull(InventoryItem upd) {
         updateFull(upd.getOwner(), ItemHelper.idOf(upd.getItemStack().getItem()), upd);
     }

    @SqlUpdate("INSERT INTO InventoryItem (owner, item, amount) VALUES (:id, :item, :amount)")
    void insert(@BindBean NewUser owner, @Bind("item") int itemId, @BindBean InventoryItem upd);

    @Override
    default void insert(InventoryItem ins) {
        updateFull(ins.getOwner(),  ItemHelper.idOf(ins.getItemStack().getItem()), ins);
    }

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS InventoryItem " +
                    "(" +
                    " owner     BIGINT   NOT NULL," +
                    " item     INT   NOT NULL," +
                    " amount     INT   NOT NULL," +
                    " PRIMARY KEY(owner, item)," +
                    " FOREIGN KEY(owner) REFERENCES UserData(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    default void createTable() {}

}
