package com.robo.server.mq;

import com.common.base.exception.BusinessException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jianghaoming on 17/8/24.
 */
public class EndPoint {

    protected static Channel channel;
    protected static Connection connection;

    private static String host ="";
    private static Integer port = null;
    private static String userName = "";
    private static String passWord = "";

    private static boolean initConfig = false;



    public static void init(String _host, Integer _port, String _userName, String _passWord) throws IOException, TimeoutException {
        host = _host;
        port = _port;
        userName = _userName;
        passWord = _passWord;


        if(connection == null || channel == null || !connection.isOpen() || !channel.isOpen()) {
            getConnection();

        }
        initConfig = true;
    }


    public EndPoint(String exchageName) throws IOException, TimeoutException, BusinessException {

        if(exchageName == null || exchageName.equals("")){
            throw new BusinessException("队列名称不能为空");
        }

        if(!initConfig){
            throw new BusinessException("请先初始化配置");
        }

        if(channel.isOpen()) {
            //creating a channel
            channel = connection.createChannel();
            //declaring a queue for this channel. If queue does not exist,
            //it will be created on the server.
            //channel.queueDeclare(endpointName, false, false, false, null);
            channel.exchangeDeclare(exchageName,"topic");
        }else{
            getConnection();
        }
    }


    /**
     *
     * @throws IOException
     */
    public void close() throws IOException, TimeoutException {
        this.channel.close();
        this.connection.close();
    }

    protected static void getConnection() throws IOException, TimeoutException {
        //Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();

        //hostname of your rabbitmq server
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(userName);
        factory.setPassword(passWord);

        //getting a connection
        connection = factory.newConnection();

        //creating a channel
        channel = connection.createChannel();
    }
}
