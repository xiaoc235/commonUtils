package com.common.base;


import com.common.base.annotation.CanNullAnnotation;

/**
 * Created by jianghaoming on 17/2/24.
 *
 *  基本的数据请求model
 */
public class BaseRequestModel extends BaseDto {


    @CanNullAnnotation(isCanNull = false, returnMessage = "用户编号不能为空")
    private String userId;

    @CanNullAnnotation(isCanNull = false, returnMessage = "token不能为空")
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
