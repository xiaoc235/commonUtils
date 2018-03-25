package com.xc.wechat.model.order;

import com.google.gson.annotations.SerializedName;
import com.xc.wechat.base.BaseResponseModel;

public class OrderGenerateResponseModel extends BaseResponseModel {

    /**
     * 以下字段在return_code 和result_code都为SUCCESS的时候有返回
     */
    @SerializedName("trade_type")
    private String tradeType;

    @SerializedName("prepay_id")
    private String prepayId;

    @SerializedName("code_url")
    private String codeUrl;

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
