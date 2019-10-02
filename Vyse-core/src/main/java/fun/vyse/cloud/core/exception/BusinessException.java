package fun.vyse.cloud.core.exception;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException() {

    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(message != null && !message.equals("") ? message : String.valueOf(code));
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
