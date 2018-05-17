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

    public static boolean getBoolean(int _value){
        if(_value == YES.getIntValue()){
            return true;
        }
        return false;
    }

    public static int getInt(boolean flag){
        if(flag){
            return YES.getIntValue();
        }else{
            return NO.getIntValue();
        }
    }


}
