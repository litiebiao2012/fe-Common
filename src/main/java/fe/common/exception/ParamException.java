package fe.common.exception;

/**
 * Created by fe on 2017/1/18.
 */
public class ParamException extends RuntimeException {

    protected String exceptionCode;
    /**
     *
     */
    private static final long serialVersionUID = -2357521295745486102L;

    public ParamException() {
        super();
    }

    public ParamException(String code,String message) {
        super(message);
        this.exceptionCode = code;
    }

    public ParamException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
    public ParamException(String msg,Throwable throwable,String code) {
        super(msg, throwable);
        this.exceptionCode = code;
    }

    public ParamException(String msg) {
        super(msg);
    }

    public ParamException(Throwable arg0) {
        super(arg0);
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}

