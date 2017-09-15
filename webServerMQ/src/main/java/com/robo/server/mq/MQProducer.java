package com.robo.server.mq;

import com.common.base.exception.BusinessException;
import com.common.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jianghaoming on 17/8/24.
 */
public class MQProducer extends EndPoint{

    private static final Logger _logger = LoggerFactory.getLogger(MQProducer.class);


    public MQProducer(String exchageName) throws BusinessException, TimeoutException, IOException {
        super(exchageName);
    }

    public void sendMessage(Object object,String exchageName, String routingKey) throws IOException, TimeoutException, BusinessException {
        if(channel.isOpen()) {
            String jsonResult = GsonUtils.toJson(object);
            _logger.info("mq request jonsResult ==> " + jsonResult);
            channel.basicPublish(exchageName, routingKey, null, jsonResult.getBytes());
        }else{
            _logger.info("channel is close");
            getConnection();
            _logger.info("channle is reconnection");

        }
    }
}