package net.kodehawa.mantarobot.db.rel.dao;

import net.kodehawa.mantarobot.db.rel.help.Transactional;

public interface DataDao<T extends Transactional> {

    void createTable();
    void delete(T del);
    void updateFull(T upd);
    void insert(T ins);

}
