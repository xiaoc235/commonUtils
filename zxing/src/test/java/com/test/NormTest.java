package com.test;

import com.common.zxing.BarcodeUtils;
import org.junit.Test;


public class NormTest {

    @Test
    public void test1(){
        String url = "http://m.guoranedu.com/jump?url=group-detail&courseId=2&fromUser=share_user_%s";
        String logoPath = "D:\\q\\%s.png";
        String qrCodePath = "D:\\q\\code\\%s.jpg";

        for(int i = 1; i<=60; i++){
            String str = i<10 ? "0"+i : i+"";
            String tempUrl = String.format(url, str);
            BarcodeUtils.encode(tempUrl, String.format(logoPath, i),String.format(qrCodePath,str));
        }

    }
}
