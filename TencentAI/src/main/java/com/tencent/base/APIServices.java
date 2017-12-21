package com.tencent.base;

import java.util.Map;

/**
 * Created by jianghaoming on 2017/12/21.
 */
public class APIServices {

    private static final String baseAIUrl = "https://api.ai.qq.com/fcgi-bin/";

    /**
     * ORC图片识别接口
     */
    public static class ORCUrl{
        public static final String commonORC = baseAIUrl + "/ocr/ocr_generalocr";
    }





    protected static Map<String,Object> buildParamMap(Map<String,Object> paramMap){
        return Auth.buildParamMap(paramMap);
    }

}
