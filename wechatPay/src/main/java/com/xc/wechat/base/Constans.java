package com.xc.wechat.base;

/**
 * 基本常量类
 */
public class Constans {

    public static class API{
        public static String QRCodeUrl = "weixin://wxpay/bizpayurl?sign={sign}&appid={appid}&mch_id={mch_id}&product_id={product_id}&time_stamp={time_stamp}&nonce_str={nonce_str}";
        public static String GenerateOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    }
}
