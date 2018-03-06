package com.xc.wechat.model.order;

import com.common.base.annotation.CanNullAnnotation;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.xc.wechat.base.BaseResultModel;


/**
 * doc : https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
 */
public class OrderGenerateRequestModel extends BaseResultModel{

    //自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
    @XStreamAlias("device_info")
    private String deviceInfo;

    private String body; //商品简单描述

    @CanNullAnnotation
    private String detail; //商品详情

    @CanNullAnnotation
    private String attach; //附加自定义数据

    @XStreamAlias("out_trade_no")
    private String outTradeNo; //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一

    @CanNullAnnotation
    @XStreamAlias("fee_type")
    private String feeType;//	符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型

    @XStreamAlias("total_fee")
    private Integer totalFee; //订单总金额，单位为分，详见支付金额

    @XStreamAlias("spbill_create_ip")
    private String spbillCreateIp; //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP

    @CanNullAnnotation
    @XStreamAlias("time_start")
    private String timeStart;//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则

    /**
     *订单失效时间，
     // 格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
     // 订单失效时间是针对订单号而言的，
     // 由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，
     // 所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
     //建议：最短失效时间间隔大于1分钟
     */
    @CanNullAnnotation
    @XStreamAlias("time_expire")
    private String timeExpire;

    @CanNullAnnotation
    @XStreamAlias("goods_tag")
    private String goodsTag; //订单优惠标记，使用代金券或立减优惠功能时需要的参数

    @XStreamAlias("notify_url")
    private String notifyUrl; //异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。

    @XStreamAlias("trade_type")
    private String tradeType; //JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付

    @CanNullAnnotation
    @XStreamAlias("product_id")
    private String productId;//trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义

    @CanNullAnnotation
    @XStreamAlias("limit_pay")
    private String limitPay;

    @CanNullAnnotation
    @XStreamAlias("openid")
    private String openId;


    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    @Override
    public String getOpenId() {
        return openId;
    }

    @Override
    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
