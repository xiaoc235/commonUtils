package com.common.em;

/**
*  0:否 1:是
 */
public enum CommResultEnum {
    YES("1"),
    NO("0");

    private String value ;

    private CommResultEnum(final String _value) {
        this.value = _value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue(){
        return Integer.parseInt(value);
    }


}
