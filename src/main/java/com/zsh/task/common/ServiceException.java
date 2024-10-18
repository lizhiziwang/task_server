package com.zsh.task.common;



public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;
    private int code;

    public ServiceException() {
        this.code = Result.ERROR_CODE;
    }

    public ServiceException(BaseExceptionMsg baseExceptionMsg) {
        this(baseExceptionMsg.getExpCode(), baseExceptionMsg.getExpMsg());
    }

    public ServiceException(String customMessage, BaseExceptionMsg baseExceptionMsg) {
        this(baseExceptionMsg.getExpCode(), customMessage);
    }

    public ServiceException(BaseExceptionMsg baseExceptionMsg, Object... values) {
        this(baseExceptionMsg.getExpCode(), baseExceptionMsg.getExpMsg(), values);
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = Result.ERROR_CODE;
        this.code = code;
    }

    public ServiceException(int code, String message, Object... values) {
        this(code, getFormatMessage(message, values));
    }

    public ServiceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = Result.ERROR_CODE;
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
        this.code = Result.ERROR_CODE;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = Result.ERROR_CODE;
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.code = Result.ERROR_CODE;
    }

    private static String getFormatMessage(String message, Object... values) {
        if (null != values) {
            message = String.format(message, values);
        }

        return message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
