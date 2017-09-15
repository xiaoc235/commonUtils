package com.common.em;

import java.util.HashMap;
import java.util.Map;

/**
*  0:否 1:是
 */
public enum CommResultEnum {
    YES("1"),
    NO("0");

    private String value ;

    private static Map<String, String> desc = new HashMap<String, String>();
    static {
        desc.put(YES.getValue(),"是");
        desc.put(NO.getValue(),"否");
    }

    private CommResultEnum(final String _value) {
        this.value = _value;
    }
    public static Map<String,String> getAllMap(){
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.putAll(desc);
        return resultMap;
    }
    public String getValue() {
        return value;
    }

    public static String getDesc(final String key) {
        if(desc.containsKey(key)){
            return desc.get(key);
        }else{
            return "";
        }
    }

}
