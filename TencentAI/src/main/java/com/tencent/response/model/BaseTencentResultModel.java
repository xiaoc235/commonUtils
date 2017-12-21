package com.tencent.response.model;

import com.common.base.BaseDto;

import java.util.Map;

/**
 * Created by jianghaoming on 2017/12/21.
 *
 *
 小于0	表示系统出错，例如网络超时；一般情况下需要发出告警，共同定位问题原因。
 等于0	表示处理成功
 大于0	表示业务出错，例如调用者传递非法参数；不同业务错误有单独的返回码定义
 */
public class BaseTencentResultModel extends BaseDto{

    private String ret;

    private String msg;

    private Map<String,Object> data;


    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
