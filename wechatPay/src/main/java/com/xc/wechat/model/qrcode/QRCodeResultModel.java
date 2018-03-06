package com.xc.wechat.model.qrcode;

import com.google.gson.annotations.SerializedName;
import com.xc.wechat.base.BaseResultModel;

/**
 * 扫码返回结果
 */
public class QRCodeResultModel extends BaseResultModel{
    @SerializedName("is_subscribe")
    private String isSubscribe; //是否关注公众号

    @SerializedName("product_id")
    private String productId; //商品id

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
