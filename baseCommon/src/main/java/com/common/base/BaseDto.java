package com.common.base;


import com.common.base.annotation.CanNullAnnotation;
import com.common.base.exception.BusinessException;
import com.common.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by jianghaoming on 17/2/24.
 */
public class BaseDto implements Serializable {


    private static final long serialVersionUID = 2935363275944294319L;

    public BaseDto(){
        super();
    }
    /**
     * @Title: 输出日志字段
     * @author jianghaoming
     * @date 2016/10/12  11:36
     */
    public String toLogger() {
        StringBuffer loggerResult = new StringBuffer();
        Field fields[] = ReflectionUtils.getDeclaredField(this);
        for (Field fie : fields) {
            String fieldName = fie.getName();
            if(!fieldName.equals("serialVersionUID")) {
                String result = null;
                try {
                    result = ReflectionUtils.getFieldValue(this, fieldName) + "";
                    if(!("").equals(result) && null!=result && !("null").equals(result)) {
                        if(result.length()>200){
                            result = result.length()+"";
                            fieldName = fieldName+".size";
                        }
                        loggerResult.append(fieldName + "=[" + result + "]   ");
                    }
                } catch (NoSuchFieldException e) {
                    //just catch the exception to avoid error
                }
            }
        }
        return loggerResult.toString();
    }

    @Override
    public String toString() {
        return this.toLogger();
    }

    /**
     * @Title: 验证属性是否为空
     * @author jianghaoming
     * @date 2016/10/18  16:46
     */
    public void checkFieldNull() throws BusinessException, NoSuchFieldException {
        Field fields[] = ReflectionUtils.getDeclaredField(this);
        for (Field fie : fields) {
            boolean isCanNull = false; //是否可以为空
            String message ="";
            //获取注解
            for (Annotation anno : fie.getDeclaredAnnotations()){
                if(anno.annotationType().equals(CanNullAnnotation.class)){
                    isCanNull = ((CanNullAnnotation)anno).isCanNull();
                    message = ((CanNullAnnotation)anno).returnMessage();
                    break;
                }
            }
            //获取属性
            final String fieldName = fie.getName();
            if(!fieldName.equals("serialVersionUID") && !isCanNull) {
                String result = ReflectionUtils.getFieldValue(this, fieldName) + "";
                if(message == null || message.equals("") || message.equals("null")){
                    message = fieldName+"不能为空";
                }

                if(StringUtils.isBlank(result)){
                    throw new BusinessException(message);
                }else if("null".equals(result.toLowerCase())){
                    throw new BusinessException(message);
                }
            }
        }
    }


    /**
     * null替换为""
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void NullReplaceBlank() throws BusinessException, NoSuchFieldException {
        Field fields[] = ReflectionUtils.getDeclaredField(this);
        for (Field fie : fields) {
            //获取属性
            String fieldName = fie.getName();
            if(!fieldName.equals("serialVersionUID")) {
                String result = ReflectionUtils.getFieldValue(this, fieldName) + "";
                if(("null").equals(result)) {
                    if(fie.getType().getName().equals("java.lang.String")) {
                        ReflectionUtils.setFieldValue(this, fieldName, "");
                    }else if(fie.getType().getName().equals("java.lang.Integer")){
                        ReflectionUtils.setFieldValue(this, fieldName, 0);
                    }

                }
            }
        }
    }

}
