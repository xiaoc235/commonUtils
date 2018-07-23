package com.common.spring.utils;

import com.common.base.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jianghaoming on 17/2/27.
 */
public class CheckUtils {

    private CheckUtils(){
        super();
    }

    /**
     * @Title: 校验参数为空
     * @author jianghaoming
     * @param  param 参数，message 提示信息
     */
    public static void checkParamNull(final String param, final String message) throws BusinessException {
        if(CommonUtils.isBlank(param)){
            throw new BusinessException(message);
        }else if("null".equalsIgnoreCase(param)){
            throw new BusinessException(message);
        }
    }

    public static void checkParamNullWithRetCode(final String param, final String message) throws BusinessException {
        if(CommonUtils.isBlank(param)){
            throw new BusinessException(HttpServletResponse.SC_BAD_REQUEST, message);
        }else if("null".equalsIgnoreCase(param)){
            throw new BusinessException(HttpServletResponse.SC_BAD_REQUEST, message);
        }
    }
    public static void checkMoible(final String mobile) throws BusinessException{
        checkParamNull(mobile,"手机号码不能为空");
        if(!CommonUtils.isMobile(mobile)){
            throw new BusinessException("请输入正确的手机号码");
        }
    }



}
