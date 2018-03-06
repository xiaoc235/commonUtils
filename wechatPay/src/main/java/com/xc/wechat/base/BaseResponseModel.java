package com.xc.wechat.base;

import com.common.base.BaseDto;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * API基本返回
 */
public class BaseResponseModel extends BaseDto{

    @XStreamAlias("return_code")
    @SerializedName("return_code")
    private String returnCode;

    @XStreamAlias("return_msg")
    @SerializedName("return_msg")
    private String returnMsg;


    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
