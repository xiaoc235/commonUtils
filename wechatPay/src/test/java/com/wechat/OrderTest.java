package com.wechat;

import com.common.base.exception.BusinessException;
import com.xc.wechat.config.ConfigKey;
import com.xc.wechat.model.order.OrderGenerateRequestModel;
import com.xc.wechat.pay.OrderManager;
import com.xc.wechat.pay.QRCode;
import com.xc.wechat.utils.WXUtils;
import org.junit.Test;

public class OrderTest {

    @Test
    public void generateOrder() throws Exception {
        OrderGenerateRequestModel requestModel = new OrderGenerateRequestModel();
        requestModel.setAttach("");
        requestModel.setBody("这是详情");
        OrderManager.unifiedorder(requestModel);
    }

    @Test
    public void testUtisl(){
        OrderGenerateRequestModel requestModel = new OrderGenerateRequestModel();
        requestModel.setOpenId("ddd");
        requestModel.setMchId("4432");
        String str = WXUtils.ObjectToXML(requestModel);
        System.out.println(str);
    }
}
