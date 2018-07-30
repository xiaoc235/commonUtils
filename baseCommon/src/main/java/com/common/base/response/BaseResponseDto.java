package com.common.base.response;

import com.common.base.BaseDto;

import java.util.HashMap;


public class BaseResponseDto<T> extends BaseDto {

    private boolean success = false;
    private int code; //对应HttpServletResponse中的状态码
    private String message;
    private T data;

    public BaseResponseDto( Boolean success , int code, String message, T data) {
        super();
        this.success = success;
        this.code = code;
        this.message = message;
        if(data != null) {
            this.data = data;
        }
    }

    public BaseResponseDto() {
        super();
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
