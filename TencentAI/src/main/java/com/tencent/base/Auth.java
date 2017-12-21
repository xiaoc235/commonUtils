package com.tencent.base;

import com.common.utils.MD5Util;
import com.tencent.config.InitConfig;

import java.net.URLEncoder;
import java.util.*;

/**
 * api鉴权
 * Created by jianghaoming on 2017/12/21.
 *
 算法实现
 签名算法采用MD5摘要方式实现，步骤如下：

 将参数对按key进行字典升序排序，得到有序的参数对列表N
 将列表N中的参数对按URL键值对的格式拼接成字符串，得到字符串T（如：key1=val1&key2=val2），值使用URL编码
 将应用密钥以app_key为键名，组成URL键值拼接到字符串T末尾，得到字符串S（如：key1=val1&key2=val2&app_key=密钥)
 对字符串S进行MD5摘要计算，将得到的md5值转换成大写，最终得到接口请求签名

 */
public class Auth {
    private static String appId = InitConfig.getConfig().getAppId();
    private static String appKey = InitConfig.getConfig().getAppKey();
    private static Random random = new Random();

    public static Map<String,Object> buildParamMap(Map<String,Object> paramMap)  {
        Map<String,Object> commonMap = new HashMap<>();
        commonMap.put("app_id",appId);
        commonMap.put("nonce_str", random.nextInt(1000));
        commonMap.put("time_stamp",(System.currentTimeMillis()/1000));
        if(paramMap!=null && paramMap.size() > 0) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                try {
                    commonMap.put(entry.getKey(), URLEncoder.encode(entry.getValue()+"", "utf-8"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        commonMap.put("sign",sign(commonMap));
        return commonMap;
    }

    private static String sign(Map<String,Object> paramMap){
        String str = "";
        //字典排序
        Collection<String> keyset= paramMap.keySet();
        List<String> list=new ArrayList<>(keyset);
        Collections.sort(list);
        for(String key : list){
            if(str.equals("")) {
                str = key + "=" + paramMap.get(key);
            }else{
                str = str + "&"+key + "=" + paramMap.get(key);
            }
        }

        str = str + "&app_key="+appKey;
        String sign = MD5Util.string2MD5(str).toUpperCase();
        return sign;
    }





}
