package com.xc.wechat.model.order;

import com.google.gson.annotations.SerializedName;
import com.xc.wechat.base.BaseResponseModel;


/*******************************************************************
 *
 *  doc : https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 *
 <xml><appid><![CDATA[wxade0dfdd678a58f2]]></appid>
 <bank_type><![CDATA[CFT]]></bank_type>
 <cash_fee><![CDATA[1]]></cash_fee>
 <device_info><![CDATA[WEB]]></device_info>
 <fee_type><![CDATA[CNY]]></fee_type>
 <is_subscribe><![CDATA[Y]]></is_subscribe>
 <mch_id><![CDATA[1499303192]]></mch_id>
 <nonce_str><![CDATA[7ca72f47a19d4de2a08cc68c282e9af1]]></nonce_str>
 <openid><![CDATA[opYNl1AWJiya38lE4cYNRLOtCYBI]]></openid>
 <out_trade_no><![CDATA[QX1520499899140]]></out_trade_no>
 <result_code><![CDATA[SUCCESS]]></result_code>
 <return_code><![CDATA[SUCCESS]]></return_code>
 <sign><![CDATA[CF4A990154F2C808A166E70124D79058]]></sign>
 <time_end><![CDATA[20180308170609]]></time_end>
 <total_fee>1</total_fee>
 <trade_type><![CDATA[JSAPI]]></trade_type>
 <transaction_id><![CDATA[4200000051201803084930254979]]></transaction_id>
 </xml>
 ******************************************************************************/

/**
 * 订单支付返回参数
 * @user jianghaoming
 * @date 2018-03-08 17:24:41
 */
public class OrderPayResponseModel extends BaseResponseModel {

    @SerializedName("openid")
    private String openId;

    @SerializedName("is_subscribe")
    private String isSubscribe; //是否关注公众号

    @SerializedName("trade_type")
    private String tradeType; //交易类型

    @SerializedName("bank_type")
    private String bankType; //银行信息

    @SerializedName("total_fee")
    private Integer totalFee; //总金额

    @SerializedName("settlement_total_fee")
    private Integer settlementTotalFee; //应结订单金额

    @SerializedName("feeType")
    private String feeType; //货币种类

    @SerializedName("cash_fee")
    private Integer cashFee;//现金支付金额

    @SerializedName("cash_fee_type")
    private String cashFeeType;

    @SerializedName("transaction_id")
    private String transactionId; //微信支付订单号

    @SerializedName("out_trade_no")
    private String outTradeNo; //商户订单号

    private String attach;

    @SerializedName("time_end")
    private String timeEnd; //支付完成时间

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(Integer settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Integer getCashFee() {
        return cashFee;
    }

    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
