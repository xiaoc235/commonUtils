package com.common.base.response;

import com.common.base.BaseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adam on 2017/4/12 0012.
 */
public class BaseResponseDto extends BaseDto {

    private static final Logger _logger = LoggerFactory.getLogger(BaseResponseDto.class);

    private Boolean success;
    private int code; //对应HttpServletResponse中的状态码
    private String message;
    private Object data;

    public BaseResponseDto(final Boolean success,final int code, final String message, final Object data) {
        super();
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
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
