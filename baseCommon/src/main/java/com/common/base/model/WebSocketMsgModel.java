package com.common.base.model;

import com.common.base.BaseDto;
import com.common.base.annotation.CanNullAnnotation;

/**
 *
 * websocket 消息交互实体类
 * Created by jianghaoming on 2017/7/19  20:49
 */
public class WebSocketMsgModel extends BaseDto {

    private String messageId;

    private String userId;

    private String method;

    private String token;

    @CanNullAnnotation
    private Object date;

    private String timeStamp;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
