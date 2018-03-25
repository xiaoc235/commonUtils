package com.xc.wechat.utils;

import com.common.base.exception.BusinessException;
import com.common.utils.CommonUtils;
import com.common.utils.ReflectionUtils;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.*;

public class WXUtils {


    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5(sb.toString()).toUpperCase();

    }

    public static boolean isSignatureValid(Map<String, String> data, String key, String sign) throws Exception {
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 获得随机字符串
     * @return
     */
    public static String getNonceStr(){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }



    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }





    /**
     * 解析微信发来的请求（XML）
     */
    public static HashMap<String, String> parseXML(String result) throws Exception {
        if(CommonUtils.isBlank(result)){
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(result.getBytes("UTF-8")));
        // 得到xml根元素
        Element root = document.getRootElement();
        // 将解析结果存储在HashMap中
        HashMap<String, String> map = new HashMap<String, String>();
        recursiveParseXML(root,map);
        return map;
    }

    private static void recursiveParseXML(Element root,HashMap<String, String> map){
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        //判断有没有子元素列表
        if(elementList.size() == 0){
            map.put(root.getName(), root.getText());
        }else{
            //遍历
            for (Element e : elementList){
                recursiveParseXML(e,map);
            }
        }
    }

    /**
     * 扩展xstream使其支持CDATA
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                //@SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });


    public static String ObjectToXML(Object obj) {
        xstream.processAnnotations(obj.getClass());
        xstream.alias("xml", obj.getClass());
        return xstream.toXML(obj).replaceAll("__","_");
    }


    /**
     *  转换为Map
     */
    public static Map<String,Object> ObjectToMap(Object obj) throws BusinessException, NoSuchFieldException {
        Field fields[] = ReflectionUtils.getDeclaredField(obj);
        Map<String,Object> map = new HashMap<>();
        for (Field fie : fields) {
            String fieldName = fie.getName();
            String mapKey = fieldName;
            //获取json注解名字
            for (Annotation anno : fie.getDeclaredAnnotations()){
                if(anno.annotationType().equals(SerializedName.class)){
                    mapKey = ((SerializedName)anno).value();
                    break;
                }else if(anno.annotationType().equals(XStreamAlias.class)){
                    mapKey = ((XStreamAlias)anno).value();
                    break;
                }
            }
            if(!fieldName.equals("serialVersionUID")) {
                Object result = ReflectionUtils.getFieldValue(obj, fieldName);
                if(result != null) {
                  map.put(mapKey,result);
                }
            }
        }
        return map;
    }


    /**
     * 获取 timestamp 10位
     */
    public static String getTimeStamp(){
       return System.currentTimeMillis()/1000+"";
    }


}
