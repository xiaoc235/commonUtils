package com.xc.wechat.config;

/**
 * appKey: 5K8264ILTKCH16CQ2502SI8ZNMTM67V1
 * mchId: 1499303192
 * appId:wxade0dfdd678a58f2
 *
 *  serk: 5534535a540dd0f1ce8f7cac61f941f2
 * 回调地址:  http://pay.duilujob.net/result/wechat
 */
public class ConfigKey {

    public static String APPID = ""; //appId
    public static String MCHID = ""; //商户ID
    public static String APPKEY = "";//安全密钥

    public static void init(String _appId, String _mchId, String _appKey){
        APPID = _appId;
        MCHID = _mchId;
        APPKEY = _appKey;
    }
}
