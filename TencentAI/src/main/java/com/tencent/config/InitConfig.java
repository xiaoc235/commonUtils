package com.tencent.config;

/**
 * 基本的初始化配置类
 * Created by jianghaoming on 2017/12/21.
 */
public class InitConfig {

    private InitConfig(){
        super();
    }

    public static void init(String _appId, String _appKey){
        appId = _appId;
        appKey = _appKey;
    }

    private static String appId;
    private static String appKey;

    private static InitConfig instance;

    public static InitConfig getConfig(){
        if(instance == null){
            instance = new InitConfig();
        }
        if(appId == null){
            throw new RuntimeException("appId is null !!! ");
        }else if(appKey == null){
            throw new RuntimeException("appKey is null !!! ");
        }
        return instance;
    }

    public String getAppId(){
        return appId;
    }

    public String getAppKey(){
        return appKey;
    }
}
