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


    /**
     * 以下字段在return_code为SUCCESS的时候有返回
     */
    @SerializedName("appid")
    private String appId;

    @SerializedName("mch_id")
    private String mchId;

    @SerializedName("device_info")
    private String deviceInfo;

    @SerializedName("nonce_str")
    private String nonceStr;

    private String sign;

    @SerializedName("result_code")
    private String resultCode;

    @SerializedName("err_code")
    private String errCode;

    @SerializedName("err_code_des")
    private String errCodeDes;


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


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }
}
