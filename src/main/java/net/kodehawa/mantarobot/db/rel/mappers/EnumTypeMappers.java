package net.kodehawa.mantarobot.db.rel.mappers;

import net.kodehawa.mantarobot.commands.currency.item.ItemType;
import net.kodehawa.mantarobot.commands.currency.profile.Badge;
import net.kodehawa.mantarobot.commands.currency.profile.ProfileComponent;
import net.kodehawa.mantarobot.commands.currency.profile.inventory.InventorySortType;
import net.kodehawa.mantarobot.db.entities.done.PlayerEquipment;
import net.kodehawa.mantarobot.db.rel.help.WaifuState;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeMappers {

    public abstract static class GenericEnumMapper<T extends Enum<T>> implements ColumnMapper<T> {
        private final Class<T> type;

        protected GenericEnumMapper(Class<T> type) {
            this.type = type;
        }

        @Override
        public T map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
            int ordinal = rs.getInt(columnNumber);
            if (ordinal < 0 || ordinal >= type.getEnumConstants().length) {
                throw new SQLException("Invalid ordinal value for enum type " + type.getSimpleName());
            }
            return type.getEnumConstants()[ordinal];
        }
    }

    public static class PotionTypeMapper extends GenericEnumMapper<ItemType.PotionType> {
        public PotionTypeMapper() {
            super(ItemType.PotionType.class);
        }
    }

    public static class BadgeMapper extends GenericEnumMapper<Badge> {
        public BadgeMapper() {
            super(Badge.class);
        }
    }

    public static class PlayerEquipmentTypeMapper extends GenericEnumMapper<PlayerEquipment.EquipmentType> {
        public PlayerEquipmentTypeMapper() {
            super(PlayerEquipment.EquipmentType.class);
        }
    }

    public static class ProfileComponentMapper extends GenericEnumMapper<ProfileComponent> {
        public ProfileComponentMapper() {
            super(ProfileComponent.class);
        }
    }

    public static class InventorySortTypeMapper extends GenericEnumMapper<InventorySortType> {
        public InventorySortTypeMapper() {
            super(InventorySortType.class);
        }
    }

    public static class WaifuStateMapper extends GenericEnumMapper<WaifuState> {
        public WaifuStateMapper() {
            super(WaifuState.class);
        }
    }

}
