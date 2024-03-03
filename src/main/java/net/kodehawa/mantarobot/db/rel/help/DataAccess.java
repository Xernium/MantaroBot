package net.kodehawa.mantarobot.db.rel.help;

import com.google.common.base.Preconditions;

import net.kodehawa.mantarobot.db.rel.InventoryItem;
import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.UserBadge;
import net.kodehawa.mantarobot.db.rel.Waifu;
import net.kodehawa.mantarobot.db.rel.dao.*;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import java.util.HashMap;
import java.util.Map;


public class DataAccess {

    private final Handle trn;
    private final DataMode mode;

    // I admit this is jank, though, I don't exactly care
    private final Map<Class<? extends Transactional>, DataDao<?>> mappedDao;


    public DataAccess(Handle trn, DataMode mode) {
        this.trn = Preconditions.checkNotNull(trn);
        this.mode = Preconditions.checkNotNull(mode);
        this.mappedDao = new HashMap<>();
    }

    public DataMode getMode(){
        return mode;
    }

    public WaifuDao getWaifuAccess(){
        return (WaifuDao) mappedDao.putIfAbsent(Waifu.class, trn.attach(WaifuDao.class));
    }

    public NewUserDao getUserAccess(){
        return (NewUserDao) mappedDao.putIfAbsent(NewUser.class, trn.attach(NewUserDao.class));
    }

    public UserBadgeDao getBadgeAccess(){
        return (UserBadgeDao) mappedDao.putIfAbsent(UserBadge.class, trn.attach(UserBadgeDao.class));
    }

    public InventoryItemDao getInventoryAccess(){
        return (InventoryItemDao) mappedDao.putIfAbsent(InventoryItem.class, trn.attach(InventoryItemDao.class));
    }

    public Query createQuery(String sql) {
        return trn.createQuery(sql);
    }

    public int execute(String sql){
        return trn.execute(sql);
    }

    public Update doUpdate(CharSequence sql){
        return trn.createUpdate(sql);
    }

    public Jdbi getOwner(){
        return trn.getJdbi();
    }
}
