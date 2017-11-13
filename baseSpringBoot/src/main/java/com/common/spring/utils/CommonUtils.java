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
public class CommonUtils extends com.common.utils.CommonUtils {

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
