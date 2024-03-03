package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.commands.currency.item.Item;
import net.kodehawa.mantarobot.commands.currency.item.ItemHelper;
import net.kodehawa.mantarobot.db.entities.done.PlayerEquipment;
import net.kodehawa.mantarobot.db.rel.EquipmentItem;
import net.kodehawa.mantarobot.db.rel.InventoryItem;
import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.mappers.EnumTypeMappers;
import net.kodehawa.mantarobot.db.rel.mappers.ItemMapper;
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

public interface EquipmentItemDao extends DataDao<EquipmentItem>{

    @SqlQuery("SELECT owner, type, item, durability, autoEquip FROM EquipmentItem WHERE owner = :id")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(ItemMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.PlayerEquipmentTypeMapper.class)
    @RegisterBeanMapper(InventoryItem.class)
    Set<EquipmentItem> getUserEquipmentShallow(@BindBean NewUser owner);

    default Set<EquipmentItem> getUserEquipment(NewUser owner) {
        return getUserEquipmentShallow(owner)
                .stream()
                .peek(ele -> ele.setOwner(owner))
                .collect(Collectors.toSet());
    }

    @SqlQuery("SELECT owner, type, item, durability, autoEquip FROM EquipmentItem WHERE owner = :id AND item = :item")
    @RegisterColumnMapper(UserMappers.ShallowColumnMapper.class)
    @RegisterColumnMapper(ItemMapper.class)
    @RegisterColumnMapper(EnumTypeMappers.PlayerEquipmentTypeMapper.class)
    @RegisterBeanMapper(InventoryItem.class)
    EquipmentItem getUserEquippedShallow(@BindBean NewUser owner, @Bind("type") int type);

    default EquipmentItem getUserEquipped(NewUser owner, PlayerEquipment.EquipmentType type) {
        return getUserEquippedShallow(owner, type.ordinal());
    }

    @SqlQuery("DELETE FROM EquipmentItem WHERE owner = :id AND type = :type")
    void delete(@BindBean NewUser owner, @Bind("type") int type);

    @Override
    default void delete(EquipmentItem del) {
        delete(del.getOwner(), del.getType().ordinal());
    }

    @SqlUpdate("UPDATE EquipmentItem SET item = :item, durability = :durability, autoEquip = :autoEquip WHERE owner = :id AND type = :type")
    void updateFull(@BindBean NewUser owner, @Bind("type") int type, @Bind("item") int itemId, @BindBean EquipmentItem upd);

     @Override
     default void updateFull(EquipmentItem upd) {
         updateFull(upd.getOwner(), upd.getType().ordinal(), ItemHelper.idOf(upd.getItem()), upd);
     }

    @SqlUpdate("INSERT INTO EquipmentItem (owner, type, item, durability, autoEquip) VALUES (:id, :type, :item, :durability, :autoEquip)")
    void insert(@BindBean NewUser owner, @Bind("type") int type, @Bind("item") int itemId, @BindBean EquipmentItem upd);

    @Override
    default void insert(EquipmentItem ins) {
        updateFull(ins.getOwner(), ins.getType().ordinal(), ItemHelper.idOf(ins.getItem()), ins);
    }

    @Override
    @SqlUpdate(
            "CREATE TABLE IF NOT EXISTS EquipmentItem " +
                    "(" +
                    " owner    BIGINT  NOT NULL," +
                    " type    INT  NOT NULL," +
                    " item    INT  NOT NULL," +
                    " durability   INT  NOT NULL," +
                    " autoEquip   BOOLEAN  NOT NULL," +
                    " PRIMARY KEY(owner, type)," +
                    " FOREIGN KEY(owner) REFERENCES UserData(id) MATCH SIMPLE" +
                    "  ON UPDATE CASCADE" +
                    "  ON DELETE CASCADE" +
                    ");")
    default void createTable() {}

}
