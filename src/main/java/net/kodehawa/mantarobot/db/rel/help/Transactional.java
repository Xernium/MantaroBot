package net.kodehawa.mantarobot.db.rel.help;

import com.google.common.base.Preconditions;

public abstract class Transactional {

    private DataAccess activeTransaction;

    protected Transactional(){}

    protected Transactional(DataAccess activeTransaction) {
        this.activeTransaction = activeTransaction;
    }

    protected final <T> boolean enqueueUpdate(String attrName, T toUpdate){
        Preconditions.checkArgument(activeTransaction.getMode().shouldAllowWrite(), "Not a write transaction");

        return true;
    }



    public DataAccess getActiveTransaction() {
        return activeTransaction;
    }

    public void setActiveTransaction(DataAccess activeTransaction) {
        this.activeTransaction = activeTransaction;
    }

    //public abstract boolean update();

    //public abstract boolean delete();
    //public abstract boolean insert();
    //public abstract boolean retrieve();

}
