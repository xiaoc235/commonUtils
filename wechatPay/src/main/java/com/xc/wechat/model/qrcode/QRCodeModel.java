package com.xc.wechat.model.qrcode;

import com.google.gson.annotations.SerializedName;
import com.xc.wechat.base.BaseWechatAPIModel;

public class QRCodeModel extends BaseWechatAPIModel{

    @SerializedName("time_stamp")
    private String timeStamp;//系统当前时间 10位

    @SerializedName("product_id")
    private String productId; //商品id

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
