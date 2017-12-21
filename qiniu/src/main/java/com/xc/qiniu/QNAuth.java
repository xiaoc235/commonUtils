package com.xc.qiniu;

import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;


/**
 * 验证
 * Created by jianghaoming on 2017/11/28.
 */
public class QNAuth {

    private static String accessKey;
    private static String secretKey;
    private static String bucket;

    private void QNAuth(){

    }

    public static void initConfig(String _accessKey, String _secretKey, String _bucket){
        accessKey = _accessKey;
        secretKey = _secretKey;
        bucket = _bucket;
    }

    /**
     * 获取认证token
     * @param expires 有效时间，单位:秒
     * @return
     */
    public static String getAuthToken(Long expires){
        if(StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey) || StringUtils.isNullOrEmpty(bucket) ){
            throw new RuntimeException("accessKey or secretKey or bucket is null");
        }
        Auth auth = Auth.create(accessKey,secretKey);
        if(expires == null) {
            return auth.uploadToken(bucket);
        }else{
            return auth.uploadToken(bucket,"xc",expires,null);
        }
    }


    /**
     * 获取认证token
     * @return
     */
    public static String getAuthToken(){
        return getAuthToken(null);
    }
}
