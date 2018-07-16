package com.leichao.retrofit.result;

/**
 * 接口返回结果
 * Created by leichao on 2017/3/3.
 */

public class HttpResult<T> {

    private transient Status status;
    private transient String jsonStr;
    private String code = "";
    private String message = "";
    private boolean type = true;
    private T result;

    public enum Status {
        TYPE_TRUE,// 后台code返回了"success"
        TYPE_FALSE,// 后台code返回了非"success"
        ERROR_NET,// 网络异常
        ERROR_JSON,// Gson解析错误，需在上线前避免此类异常
        ERROR_OTHER// 服务器程序异常或安卓端程序异常，需在上线前避免此类异常
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
