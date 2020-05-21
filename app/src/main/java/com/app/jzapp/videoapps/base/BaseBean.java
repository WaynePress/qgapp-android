package com.app.jzapp.videoapps.base;

public class BaseBean<T> {
    public T data;
    public String code;
    public String msg;

    @Override
    public String toString() {
        return "BaseBean{" +
                "data=" + data +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
