package com.xc.wechat.model.order;

import com.google.gson.annotations.SerializedName;
import com.xc.wechat.base.BaseResponseModel;

public class OrderGenerateResponseModel extends BaseResponseModel {


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


    /**
     * 以下字段在return_code 和result_code都为SUCCESS的时候有返回
     */
    @SerializedName("trade_type")
    private String tradeType;

    @SerializedName("prepay_id")
    private String prepayId;

    @SerializedName("code_url")
    private String codeUrl;


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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
