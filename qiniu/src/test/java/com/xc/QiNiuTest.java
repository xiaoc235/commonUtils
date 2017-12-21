package com.xc;

import com.xc.qiniu.QNAuth;

/**
 * Created by jianghaoming on 2017/11/28.
 */
public class QiNiuTest {

    private static String accessKey = "EZuUEi89Xb3P4Tx5-gw_hDniyEHqYBvPcvU00FTs";
    private static String secretKey = "ga74Y_2lQSvLaYZCBR88e47hIEzc93Y7EXdFby4x";
    private static String bucket = "xiaoc";

    public void testAuth(){
        QNAuth.initConfig(accessKey,secretKey,bucket);
        String token = QNAuth.getAuthToken();
        System.out.println(token);
    }


    public static void main(String[] args) {
        QiNiuTest test = new QiNiuTest();
        test.testAuth();
    }
}
