package com.xc.wechat.pay;


import com.common.base.exception.BusinessException;
import com.common.utils.GsonUtils;
import com.common.utils.HttpClientUtils;
import com.xc.wechat.base.Constans;
import com.xc.wechat.em.PayResultEm;
import com.xc.wechat.model.order.OrderGenerateRequestModel;
import com.xc.wechat.model.order.OrderGenerateResponseModel;
import com.xc.wechat.model.order.OrderPayResponseModel;
import com.xc.wechat.model.qrcode.QRCodeResultModel;
import com.xc.wechat.utils.WXUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 订单接口
 * @user jianghaoming
 * @date 2018-03-06 15:19:50
 */
public class OrderManager {

    private final static Logger _logger = LoggerFactory.getLogger(OrderManager.class);

    private static final String _generateOrderUrl = Constans.API.GenerateOrderUrl;

    /**
     * 统一下单操作
     */
    public static OrderGenerateResponseModel unifiedorder(OrderGenerateRequestModel requestModel) throws Exception {
        String xmlRequest = WXUtils.ObjectToXML(requestModel);
        HttpEntity paramEntity = new StringEntity(xmlRequest, ContentType.TEXT_XML.getMimeType(),"utf-8");
        _logger.info("xml request:{}",xmlRequest);
        String result = HttpClientUtils.getMethodPostResponse(_generateOrderUrl,paramEntity);
        _logger.info("下单返回结果:{}",result);
        Map<String,String> xmlMap = WXUtils.parseXML(result);
        OrderGenerateResponseModel responseModel = GsonUtils.convertObj(GsonUtils.toJson(xmlMap),OrderGenerateResponseModel.class);
        if(PayResultEm.FAIL.getValue().equals(responseModel.getReturnCode())){
            _logger.info("下单接口访问失败:{}",responseModel.getReturnMsg());
            throw new BusinessException("下单接口访问失败,"+ responseModel.getReturnMsg());
        }
        if(PayResultEm.FAIL.getValue().equals(responseModel.getResultCode())){
            _logger.info("下单交易失败:{} - {}",responseModel.getErrCode(), responseModel.getErrCodeDes());
            throw new BusinessException("下单交易失败:"+responseModel.getErrCode()+" - "+ responseModel.getErrCodeDes());
        }
        _logger.info("下单交易成功:{}",responseModel.toLogger());
        return responseModel;
    }


    /**
     * 支付结果回调
     */
    public static OrderPayResponseModel payResult(String xmlResult) throws Exception {
        Map<String,String> xmlMap = WXUtils.parseXML(xmlResult);
        OrderPayResponseModel responseModel = GsonUtils.convertObj(GsonUtils.toJson(xmlMap),OrderPayResponseModel.class);
        if(PayResultEm.FAIL.getValue().equals(responseModel.getReturnCode())){
            _logger.info("支付接口回调失败:{}",responseModel.getReturnMsg());
            throw new BusinessException("支付接口回调失败,"+ responseModel.getReturnMsg());
        }
        if(PayResultEm.FAIL.getValue().equals(responseModel.getResultCode())){
            _logger.info("支付交易失败:{} - {}",responseModel.getErrCode(), responseModel.getErrCodeDes());
            throw new BusinessException("支付交易失败:"+responseModel.getErrCode()+" - "+ responseModel.getErrCodeDes());
        }
        _logger.info("支付交易成功:{}",responseModel.toLogger());
        return responseModel;
    }


}
