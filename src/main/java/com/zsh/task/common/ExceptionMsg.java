package com.zsh.task.common;

public enum ExceptionMsg implements BaseExceptionMsg {
    SUCCESS(200, "成功"),
    ERROR_PARAM_MISS(397, "缺少请求参数"),
    ERROR_PARAM_RESOLVE(398, "参数解析失败"),
    ERROR_PARAM_VALID(399, "参数验证失败"),
    PARAM_ERROR(400, "参数错误"),
    AUTH_ERROR(401, "身份验证失败"),
    NO_ACCESS(402, "无权访问"),
    RESOURCE_NOT_FOUND(403, "权限资源未找到"),
    OBJECT_NOT_FOUND(404, "接口不存在"),
    ERROR_METHOD_NOT_SUPPORT(405, "请求方法不支持"),
    CONCURRENT_CONFLICT(409, "并发冲突"),
    RESOURCE_ALREADY_EXISTS(419, "试图创建的资源已存在"),
    REQUESTS_TOO_FREQUENT(429, "请求过于频繁"),
    CLIENT_CANCEL_REQUEST(499, "客户端取消请求"),
    SERVER_EXCEPTION(500, "服务异常"),
    UNKNOWN_EXCEPTION(509, "未知异常，请联系管理员"),
    BUSINESS_EXCEPTION(110000, "业务异常");

    private int code;
    private String msg;

    private ExceptionMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getExpCode() {
        return this.code;
    }

    public String getExpMsg() {
        return this.msg;
    }
}
