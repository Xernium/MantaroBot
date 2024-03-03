package net.kodehawa.mantarobot.db.rel.help;

public class DatabaseRollbackException extends RuntimeException {

    private static final DatabaseRollbackException instance = new DatabaseRollbackException();
    private DatabaseRollbackException(){}

    public static void throwThis() {
        throw instance;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;  // Override to do nothing and return 'this'
    }
}