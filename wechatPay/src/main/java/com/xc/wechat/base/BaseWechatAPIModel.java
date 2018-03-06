package com.xc.wechat.base;

import com.common.base.BaseDto;
import com.google.gson.annotations.SerializedName;
import com.xc.wechat.config.ConfigKey;

public class BaseWechatAPIModel extends BaseDto {

    @SerializedName("mch_id")
    private String mchId = ConfigKey.MCHID; //商户号

    @SerializedName("appid")
    private String appId = ConfigKey.APPID; //公众帐号Id

    @SerializedName("nonce_str")
    private String nonceStr; //随机字符串

    private String sign; //签名字符串

    @SerializedName("sign_type")
    private String signType="MD5"; //签名类型，默认为MD5，支持HMAC-SHA256和MD5。


    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }
}
