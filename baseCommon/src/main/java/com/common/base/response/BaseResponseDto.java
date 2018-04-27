package com.common.base.response;

import com.common.base.BaseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by Adam on 2017/4/12 0012.
 */
public class BaseResponseDto extends BaseDto {

    private Boolean success;
    private int code; //对应HttpServletResponse中的状态码
    private String message;
    private Object data;

    public BaseResponseDto(final Boolean success,final int code, final String message, final Object data) {
        super();
        this.success = success;
        this.code = code;
        this.message = message;
        if(data != null) {
            this.data = data;
        }else{
            this.data = new HashMap<>();
        }
    }

    public BaseResponseDto() {
        super();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
