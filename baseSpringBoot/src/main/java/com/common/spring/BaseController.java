package com.common.spring;


import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import com.common.base.response.BaseResponseDto;
import com.common.em.CommStatusEnum;
import com.common.redis.RedisClient;
import com.common.spring.utils.CheckUtils;
import com.common.spring.utils.CommonUtils;
import com.common.utils.EmojiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianghaoming on 17/2/26.
 */
public class BaseController {


    private static final Logger _baseControllerLogger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected RedisClient redis;

    protected boolean isBlank(final String param){
        return CommonUtils.isBlank(param);
    }


    /**
     * @Title: 校验参数为空
     * @author jianghaoming
     * @date 2016/9/21  9:54
     * @param  param 参数，message 提示信息
     */
    protected void checkParamNull(final String param, final String message) throws BusinessException {
        CheckUtils.checkParamNull(param,message);
    }

    /**
     * 获取request参数
     * @param key
     * @return
     */
    protected String getRequestParam(final String key){
        return request.getParameter(key);
    }

    /**
     * 获取request参数，并验证是否为空
     * @param key
     * @return
     */
    protected String getRequestParamAndCheckNull(final String key) throws BusinessException {
        final String param = getRequestParam(key);
        checkParamNull(param,key+"不能为空");
        return param;
    }
    protected Integer getRequestParamIntAndCheckNull(final String key) throws BusinessException {
        final String param = getRequestParamAndCheckNull(key);

        return Integer.parseInt(param);
    }

    /**
     * 获取map的参数值
     * @param key
     * @return
     */
    protected String getMapParam(final String key, Map<String,Object> paramMap){
        if (paramMap == null || paramMap.isEmpty() || StringUtils.isBlank(key)
                || !paramMap.containsKey(key)) {
            return null;
        }
        Object obj = paramMap.get(key);
        if (obj == null) {
            return null;
        }
        String value = obj.toString();
        try {
            if(!CommonUtils.isBlank(value)){
                value = EmojiUtil.resolveToByteFromEmoji(value.trim());
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        _baseControllerLogger.info(key+" = ["+value+"]");
        return value.trim();
    }

    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return
     */
    protected String getMapParamAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParam(key,paramMap);
        checkParamNull(param,key+"不能为空");
        _baseControllerLogger.info(key+" = ["+param+"]");
        return param;
    }

    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return int
     */
    protected Integer getMapParamIntAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParamAndCheckNull(key,paramMap);
        _baseControllerLogger.info(key+" = ["+param+"]");
        return Integer.valueOf(param);
    }



    /**
     * 返回错误提示，data为空  httpCode
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> failResponse(final String message){
        return failResponse(CommStatusEnum.FAIL.getKey(),message);
    }
    public ResponseEntity<BaseResponseDto> failResponse(final int code, final String message){
        return failResponse(code,message,null);
    }

    /**
     * 返回错误提示，返回data httpCode
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> failResponse(final int code, final String message, Object obj){
        if(message.contains(CommConstants.LOGIN_OUT_MESSAGE)){
            return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),code,message,obj),HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),code,message,obj),HttpStatus.OK);
    }


    /**
     * 系统错误，data为空  httpCode 500
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> errorResponse(final int code,final String message){
        return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),code,message,null),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 系统错误，data为空  返回自定义httpStatus
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> errorResponse(HttpStatus httpStatus,final String message){
        return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(), HttpStatus.INTERNAL_SERVER_ERROR.value(),message,null),httpStatus);
    }

    public ResponseEntity<BaseResponseDto> errorResponse(final String message){
        return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),HttpStatus.INTERNAL_SERVER_ERROR.value(),message,null),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 返回正确提示
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> succResponse(final String message){
        return succResponse(message,null, null);
    }


    /**
     * 返回正确提示，返回data
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> succResponse(final String message, Object obj){
        return succResponse(message,obj,null);
    }


    /**
     * 返回正确提示，返回data & jsonKey
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> succResponse(final String message, Object obj, final String jsonKey){
        if(isBlank(jsonKey)){
            return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.SUCC.isValue(),CommStatusEnum.SUCC.getKey(),message,obj),HttpStatus.OK);
        }else {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put(jsonKey, obj);
            return new ResponseEntity<BaseResponseDto>(new BaseResponseDto(CommStatusEnum.SUCC.isValue(),CommStatusEnum.SUCC.getKey(), message, resultMap),HttpStatus.OK);

        }
    }


}
