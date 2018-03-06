package com.wechat;


import com.common.base.exception.BusinessException;
import com.xc.wechat.config.ConfigKey;
import com.xc.wechat.pay.QRCode;
import org.junit.Test;

public class QRCodeTest {

    @Test
    public void testQRcodeUrl() throws Exception {
        ConfigKey.init("wxade0dfdd678a58f2",
                "1499303192",
                "5K8264ILTKCH16CQ2502SI8ZNMTM67V1");
        System.out.println(QRCode.getProductQRCode("1111"));
    }
}
