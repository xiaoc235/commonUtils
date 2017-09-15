package com.common.spring.utils;

import com.common.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jianghaoming on 17/2/28.
 */
public class CommonUtils {

    /**
     * @Title: 手机号码验证
     * @author jianghaoming
     * @return true 通过验证， false 未通过
     */
    public static boolean isMobile(final String mobile) {
        Pattern pattern = null;
        Matcher matcher = null;
        boolean result = false;
        pattern = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$"); // 验证手机号
        matcher = pattern.matcher(mobile);
        result = matcher.matches();
        return result;
    }


    /**
     * 字符串是否为空
     * @param param
     * @return true 为空，false 不为空
     */
    public static boolean isBlank(final String param){
        if(StringUtils.isBlank(param) || "null".equals(param.toLowerCase())){
            return true;
        }
        return false;

    }

    /**
     * list是否为空
     * @param param
     * @return true 为空，false 不为空
     */
    public static boolean isBlank(List param){
        if(param == null || param.size() == 0){
            return true;
        }
        return false;
    }


    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return String
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }





    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：HELLO_WORLD->HelloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /**
     * 判断邮箱格式
     * @param email
     * @return
     */
    public static boolean isEmail(final String email){
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        return regex.matcher(email).matches();
    }


    /**
     * 保存文件
     * @param file
     * @param filePath
     * @param fileName
     */
    public static void saveFile(MultipartFile file , String filePath, String fileName) {
        BufferedOutputStream stream = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            byte[] bytes = file.getBytes();
            File filet = new File(filePath + fileName);
            stream = new BufferedOutputStream(new FileOutputStream(filet));
            stream.write(bytes);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取map的参数值
     * @param key
     * @return
     */
    public static String getMapParam(final String key, Map<String,Object> paramMap){
        if (paramMap == null || paramMap.isEmpty() || StringUtils.isBlank(key)
                || !paramMap.containsKey(key)) {
            return null;
        }
        Object obj = paramMap.get(key);
        if (obj == null) {
            return null;
        }
        String value = obj.toString();
        return value.trim();
    }


    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return
     */
    public static String getMapParamAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParam(key,paramMap);
        CheckUtils.checkParamNull(param,key+"不能为空");
        return param;
    }

    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return int
     */
    public static Integer getMapParamIntAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParamAndCheckNull(key,paramMap);
        return Integer.valueOf(param);
    }


}
