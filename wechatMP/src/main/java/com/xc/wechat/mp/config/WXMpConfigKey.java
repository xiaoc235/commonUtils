package com.xc.wechat.mp.config;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;


public class WXMpConfigKey {

    private WXMpConfigKey(){

    }

    /**
     * 设置微信公众号的appid
     */
    public static String appId;

    /**
     * 设置微信公众号的app secret
     */
    public static String secret;

    /**
     * 设置微信公众号的token
     */
    public static String token;

    /**
     * 设置微信公众号的EncodingAESKey
     */
    public static String aesKey;

    public static void initConfig(String _appId, String _secret, String _token, String _aesKey){
        appId = _appId;
        secret = _secret;
        token = _token;
        aesKey = _aesKey;
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(secret); // 设置微信公众号的app corpSecret
        config.setToken(token); // 设置微信公众号的token
        config.setAesKey(aesKey); // 设置微信公众号的EncodingAESKey
        getWxMpService().setWxMpConfigStorage(config);
    }


    private static WxMpService wxMpService = null;
    public static WxMpService getWxMpService(){
        if(wxMpService == null){
            wxMpService =  new WxMpServiceImpl();
        }
        return wxMpService;
    }




}
