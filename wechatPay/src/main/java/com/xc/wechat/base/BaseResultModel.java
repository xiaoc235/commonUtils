package com.xc.wechat.base;

import com.common.base.BaseDto;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 回调结果基本类
 */
public class BaseResultModel extends BaseDto{

    @SerializedName("mch_id")
    @XStreamAlias("mch_id")
    private String mchId; //商户号

    @SerializedName("appid")
    @XStreamAlias("appid")
    private String appId; //公众帐号Id

    @SerializedName("openid")
    @XStreamAlias("openid")
    private String openId; //openId

    @SerializedName("nonce_str")
    @XStreamAlias("nonce_str")
    private String nonceStr;

    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
