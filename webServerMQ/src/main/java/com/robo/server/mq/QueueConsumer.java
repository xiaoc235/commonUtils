package com.robo.server.mq;

import com.common.base.exception.BusinessException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jianghaoming on 17/8/24.
 */
public class QueueConsumer extends EndPoint implements Runnable, Consumer {

    private static String _exchageName = "";

    private static final Logger _logger = LoggerFactory.getLogger(QueueConsumer.class);


    public QueueConsumer(String exchageName) throws IOException, TimeoutException, BusinessException {
        super(exchageName);
        _exchageName = exchageName;
    }

    public void run() {
        try {
            //start consuming messages. Auto acknowledge messages.

            if(channel.isOpen()) {

                //声明一个随机队列
                String queueName = channel.queueDeclare().getQueue();
                //绑定
                channel.queueBind(queueName, _exchageName, "");

                channel.basicConsume(queueName, true, this);

            }else{
                _logger.info("channel is close");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes){
        try {
            if (bytes != null) {
                String jsonResult = new String(bytes);
                _logger.info("mq consumer jsonResult ==> " + jsonResult);
            }
        }catch (Exception e){
            _logger.error("mq consumer is error",e);
        }
        /*System.out.println("routingKey ==> " + envelope.getRoutingKey());
        System.out.println("Message Number "+ map.get("message number") + " received.");*/

    }

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }



}