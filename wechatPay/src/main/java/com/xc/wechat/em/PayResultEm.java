package com.xc.wechat.em;

public enum PayResultEm {

    SUCC("SUCCESS"),
    FAIL("FAIL");

    private String value;

    private PayResultEm(String _value){
        value = _value;
    }

    public String getValue() {
        return value;
    }
}
