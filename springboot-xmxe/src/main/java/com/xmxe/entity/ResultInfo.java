package com.xmxe.entity;

/**
 * 统一返回数据格式
 * @param <T>
 */
public class ResultInfo<T> {
    private Integer status;
    private String message;
    private T response;

    public ResultInfo(Integer status,String message,T response){
        this.status  = status;
        this.message = message;
        this.response = response;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
