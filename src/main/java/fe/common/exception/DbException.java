package fe.common.exception;

/**
 * Created by fe on 2017/1/20.
 */
public class DbException extends RuntimeException {
    public DbException() {
        super();
    }

    public DbException(String message) {
        super(message);
    }

    public DbException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public DbException(Throwable throwable) {
        super(throwable);
    }

}
