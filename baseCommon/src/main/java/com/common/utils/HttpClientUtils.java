package com.common.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HttpClientUtils
 */
public class HttpClientUtils {
    /**
     * 5分钟
     */
    public static final int         MINUTE_FIVE = 300000;

    /**
     * 10分钟
     */
    public static final int         MINUTE_TEN  = 600000;

    /**
     * HttpClient
     */
    private static final HttpClient client      = getInstance();

    /**
     * 让Httpclient支持https
     * 
     * @return HttpClient
     */
    private static HttpClient getInstance() {
        X509TrustManager x509mgr = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.SSL);
            sslContext.init(null, new TrustManager[] { x509mgr }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    public static final RequestConfig getDefaultTimeOutConfig() {
        return getTimeOutConfig(60000, 30000);
    }

    private static final RequestConfig getTimeOutConfig(int socketTimeout, int connectionTimeout) {
        return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectionTimeout).build();
    }

    /**
     * Get方法查询
     */
    public static String getMethodGetResponse(String address) throws Exception {
        return getMethodGetResponse(address, getDefaultTimeOutConfig());
    }

    /**
     * Post方法查询
     */
    public static String getMethodPostResponse(String address, HttpEntity paramEntity) throws Exception {
        RequestConfig config = getDefaultTimeOutConfig();
        return getMethodPostResponse(address, paramEntity, config);
    }

    /**
     * 自定义超时的Get方法查询
     */
    public static String getMethodGetResponse(String address, int connectionTimeout, int socketTimeout)
                                                                                                       throws Exception {
        return getMethodGetResponse(address, getTimeOutConfig(socketTimeout, connectionTimeout));
    }

    /**
     * 自定义超时的Post方法
     */
    public static String getMethodPostResponse(String address, HttpEntity paramEntity, int connectionTimeout,
                                               int socketTimeout) throws Exception {
        RequestConfig config = getTimeOutConfig(socketTimeout, connectionTimeout);
        return getMethodPostResponse(address, paramEntity, config);
    }

    /**
     * Post Entity
     */
    public static byte[] getMethodPostBytes(String address, HttpEntity paramEntity) throws Exception {
        return getMethodPostContent(address, paramEntity, getDefaultTimeOutConfig());
    }

    /**
     * HttpClient get方法请求返回Entity
     */
    public static byte[] getMethodGetContent(String address, Map<String,String> header) throws Exception {
        return getMethodGetContent(address, getDefaultTimeOutConfig(),header);
    }
    public static byte[] getMethodGetContent(String address) throws Exception {
        return getMethodGetContent(address, getDefaultTimeOutConfig(),null);
    }

    /**
     * HttpClient Get方法请求数据
     */
    private static String getMethodGetResponse(String address, RequestConfig config) throws Exception {
        byte[] result = getMethodGetContent(address, config, null);
        return new String(result, "utf-8");
    }

    /**
     * HttpClient Post方法请求数据
     */
    private static String getMethodPostResponse(String address, HttpEntity paramEntity, RequestConfig config)
                                                                                                             throws Exception {
        byte[] content = getMethodPostContent(address, paramEntity, config);
        String result = new String(content, "utf-8");
        return result;

    }
    
    public static String getGjjShebaoPostResponse(String address, HttpEntity paramEntity)
            throws Exception {
		byte[] content = getMethodPostContent(address, paramEntity, getDefaultTimeOutConfig());
		String result = new String(content, "utf-8");
		return result;
	
	}
    
    /**
     * HttpClient get方法请求返回Entity
     */
    private static byte[] getMethodGetContent(String address, RequestConfig config, Map<String,String> header) throws Exception {
        HttpGet get = new HttpGet(address);
        try {
            get.setConfig(config);
            if(null != header && header.size() > 0) {
                for(Map.Entry<String,String> entry : header.entrySet()){
                    get.addHeader(entry.getKey(), entry.getValue());
                }

            }
            /*get.addHeader("Cookie"," " +
                    "wxuin=2296710362; " +
                    "wxsid=xOd76PZHh52vsfGd; " +
                    "webwx_data_ticket=BtHaXQ%2F9k4Gts4AUyMwiTvFrNbgFlQn1S9daewl62xr6Fo85QDACpsf%2FnF6Llcny" +
                    "");*/

            /**
             * {'webwx_data_ticket': u'BtHaXQ%2F9k4Gts4AUyMwiTvFrNbgFlQn1S9daewl62xr6Fo85QDACpsf%2FnF6Llcny', 'wxsid': u'', 'wxuin': u'2296710362'}
             */

            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                int code = response.getStatusLine().getStatusCode();
                throw new RuntimeException("HttpGet Access Fail , Return Code(" + code + ")");
            }
            return convertEntityToBytes(response.getEntity());
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
    }

    /**
     * Post Entity
     */
    private static byte[] getMethodPostContent(String address, HttpEntity paramEntity, RequestConfig config)
                                                                                                            throws Exception {
        HttpPost post = new HttpPost(address);
        try {
            if (paramEntity != null) {
                post.setEntity(paramEntity);
            }
            post.setConfig(config);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                int code = response.getStatusLine().getStatusCode();
                //throw new RuntimeException("HttpPost Request Access Fail Return Code(" + code + ")");
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                //throw new RuntimeException("HttpPost Request Access Fail Response Entity Is null");
            }
            return convertEntityToBytes(entity);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
    }

    /**
     * 转化返回为byte数组
     * 
     * @param entity
     * @return byte[]
     * @throws Exception
     */
    private static byte[] convertEntityToBytes(HttpEntity entity) throws Exception {
        InputStream inputStream = null;
        try {
            if (entity == null || entity.getContent() == null) {
                throw new RuntimeException("Response Entity Is null");
            }
            inputStream = entity.getContent();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return out.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
