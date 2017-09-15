package com.common.em;

import java.util.HashMap;
import java.util.Map;

public enum CommStatusEnum {
    SUCC(true),
    FAIL(false);

    private boolean value ;


    private static Map<Boolean, String> desc = new HashMap<Boolean, String>();

    static {
        desc.put(SUCC.getValue(),"调用成功");
        desc.put(FAIL.getValue(),"调用失败");
    }

    private CommStatusEnum(final boolean _value) {
        this.value = _value;
    }

    public boolean getValue() {
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
