package com.leichao.retrofit.result;

/**
 * 接口返回结果
 * Created by leichao on 2017/3/3.
 */

public class MulaResult<T> {

    private transient Status status;
    private transient String jsonStr;
    private String code = "";
    private String message = "";
    private T result;

    public enum Status {
        CODE_SUCCESS,// 后台code返回了"success"
        CODE_FAILURE,// 后台code返回了非"success"
        ERROR_NET,// 网络异常
        ERROR_JSON,// Json解析异常，需在上线前避免此类异常
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
