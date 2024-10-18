package com.zsh.task.common;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private T data;
    private Integer code;
    private String message;
    public static final int SUCCESS_CODE;
    public static final int ERROR_CODE;
    public static final int PARAM_ERROR;
    public static final int UNKNOWN_ERROR_CODE;
    public static final String SUCCESS_MESSAGE = "成功";

//    public static <T> Result<T> succeed() {
//        return (Result<T>) of((Object)null, SUCCESS_CODE, "成功");
//    }
//
//    public static <T> Result<T> succeed(String msg) {
//        return of((Object)null, SUCCESS_CODE, msg);
//    }

    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, SUCCESS_CODE, msg);
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, SUCCESS_CODE, "成功");
    }

    public static <T> Result<T> of(T data, Integer code, String msg) {
        return new Result(data, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return (Result<T>) of((Object)null, ERROR_CODE, msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return of(model, ERROR_CODE, msg);
    }

    public static <T> Result<T> of(Integer code, String msg) {
        return new Result(code, msg);
    }

    public Result(ServiceException serviceException) {
        this.code = serviceException.getCode();
        this.message = serviceException.getMessage();
    }

    public Result(BaseExceptionMsg baseExceptionMsg) {
        this.code = baseExceptionMsg.getExpCode();
        this.message = baseExceptionMsg.getExpMsg();
    }

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    static {
        SUCCESS_CODE = ExceptionMsg.SUCCESS.getCode();
        ERROR_CODE = ExceptionMsg.BUSINESS_EXCEPTION.getCode();
        PARAM_ERROR = ExceptionMsg.PARAM_ERROR.getCode();
        UNKNOWN_ERROR_CODE = ExceptionMsg.UNKNOWN_EXCEPTION.getCode();
    }
}