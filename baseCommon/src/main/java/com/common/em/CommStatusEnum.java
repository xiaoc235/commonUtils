package com.common.em;


public enum CommStatusEnum {
    SUCC(0,true),
    FAIL(1,false),
    FAIL_MES(2,false);

    private boolean value ;
    private Integer key;

    private CommStatusEnum(Integer _key, Boolean _value){
        key = _key;
        value = _value;
    }

    public boolean isValue() {
        return value;
    }

    public Integer getKey() {
        return key;
    }
}
