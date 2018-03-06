package com.xc.wechat.pay;

import com.common.base.exception.BusinessException;
import com.xc.wechat.base.Constans;
import com.xc.wechat.config.ConfigKey;
import com.xc.wechat.model.qrcode.QRCodeModel;
import com.xc.wechat.utils.CheckUtil;
import com.xc.wechat.utils.WXUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码操作类
 */
public class QRCode {

    private final static Logger _logger = LoggerFactory.getLogger(QRCode.class);

    private static String _QRCodeAPIUrl = Constans.API.QRCodeUrl;

   /**
    * 获取商品二维码
    * @user jianghaoming
    * @date 2018-03-05 18:10:12
    */
    public static String getProductQRCode(String productId) throws Exception {
        _logger.info("productId:{}",productId);
        CheckUtil.checkAppIdAndMchId();
        QRCodeModel model = new QRCodeModel();
        model.setProductId(productId);
        model.setTimeStamp(System.currentTimeMillis()/1000+"");
        String nostr = WXUtils.getNonceStr();
        model.setNonceStr(nostr);
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid",model.getAppId());
        paramMap.put("mch_id",model.getMchId());
        paramMap.put("time_stamp",model.getTimeStamp());
        paramMap.put("nonce_str",model.getNonceStr());
        paramMap.put("product_id",model.getProductId());
        String sign = WXUtils.generateSignature(paramMap, ConfigKey.APPKEY);
        model.setSign(sign);

        String result = _QRCodeAPIUrl.replace("{sign}",model.getSign()).replace("{appid}",model.getAppId()).replace("{mch_id}",model.getMchId())
                .replace("{product_id}",model.getProductId()).replace("{time_stamp}",model.getTimeStamp()).replace("{nonce_str}",model.getNonceStr());
        _logger.info("qrcode:{}", result);
        return result;
    }
}
