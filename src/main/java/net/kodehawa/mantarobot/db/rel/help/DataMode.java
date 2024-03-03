package net.kodehawa.mantarobot.db.rel.help;

import org.jdbi.v3.core.transaction.TransactionIsolationLevel;

public enum DataMode {
    NO_ACCESS(TransactionIsolationLevel.NONE),
    READ_ONLY(TransactionIsolationLevel.READ_COMMITTED),
    READ_AND_HOLD(TransactionIsolationLevel.REPEATABLE_READ),
    READ_WRITE(TransactionIsolationLevel.SERIALIZABLE)
    ;


    // Rather be too cautious
    public static DataMode getDefault(){
        return READ_WRITE;
    }

    final TransactionIsolationLevel level;
    DataMode(TransactionIsolationLevel level) {
        this.level = level;
    }

    public TransactionIsolationLevel getLevel() {
        return level;
    }

    public boolean shouldAllowWrite(){
        return level == TransactionIsolationLevel.SERIALIZABLE;
    }

    public boolean isDataAccess() {
        return level != TransactionIsolationLevel.NONE;
    }
}
