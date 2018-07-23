package com.common.spring;


import com.common.base.CommConstants;
import com.common.base.exception.BusinessException;
import com.common.base.response.BaseResponseDto;
import com.common.em.CommStatusEnum;
import com.common.redis.RedisClient;
import com.common.spring.utils.CheckUtils;
import com.common.spring.utils.CommonUtils;
import com.common.utils.EmojiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianghaoming on 17/2/26.
 */
public class BaseController {

    private static final Logger _logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected RedisClient redis;

    private static final Map<String,String> nullMap = new HashMap<>();

    protected boolean isBlank(final String param){
        return CommonUtils.isBlank(param);
    }

    protected int getPageNumber(){
        int page = 0;
        String pageStr = getRequestParam("page");
        if(!isBlank(pageStr)){
            page = Integer.parseInt(pageStr);
        }
        if(page > 30){
            page = 30;
        }
        return page;
    }

    protected int getPageSize(){
        int size = 10;
        String sizeStr = getRequestParam("size");
        if(!isBlank(sizeStr)){
            size = Integer.parseInt(sizeStr);
        }
        return size;
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
        if (paramMap == null || paramMap.isEmpty() || CommonUtils.isBlank(key)
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
        return param;
    }

    /**
     * 获取map的参数值，并验证是否为空
     * @param key
     * @return int
     */
    protected Integer getMapParamIntAndCheckNull(final String key, Map<String,Object> paramMap) throws BusinessException {
        final String param = getMapParamAndCheckNull(key,paramMap);
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
        if(obj == null){
            obj = nullMap;
        }
        if(message.contains(CommConstants.LOGIN_OUT_MESSAGE)){
            return new ResponseEntity<>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),code,message,obj),HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),code,message,obj),HttpStatus.OK);
    }


    /**
     * 系统错误，data为空  httpCode 500
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> errorResponse(final int code,final String message){
        return new ResponseEntity<>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(),code,message,nullMap),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 系统错误，data为空  返回自定义httpStatus
     * @param message
     * @return
     */
    public ResponseEntity<BaseResponseDto> errorResponse(HttpStatus httpStatus,final String message){
        return new ResponseEntity<>(new BaseResponseDto(CommStatusEnum.FAIL.isValue(), HttpStatus.INTERNAL_SERVER_ERROR.value(),message,nullMap),httpStatus);
    }

    public ResponseEntity<BaseResponseDto> errorResponse(final String message){
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
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
        if(obj == null){
            obj = nullMap;
        }
        if(isBlank(jsonKey)){
            return new ResponseEntity<>(new BaseResponseDto(CommStatusEnum.SUCC.isValue(), CommStatusEnum.SUCC.getKey(), message, obj), HttpStatus.OK);
        }else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(jsonKey, obj);
            return new ResponseEntity<>(new BaseResponseDto(CommStatusEnum.SUCC.isValue(),CommStatusEnum.SUCC.getKey(), message, resultMap),HttpStatus.OK);

        }
    }

    @FunctionalInterface
    public interface Method{
        ResponseEntity<BaseResponseDto> call() throws BusinessException, Exception;
    }

    public ResponseEntity<BaseResponseDto> calls(Method method){
        try {
            return method.call();
        }catch (BusinessException ex) {
            final String message = ex.getErrorDesc();
            _logger.info(CommConstants.BUSINESS_ERROR + message,ex);
            return failResponse(ex.getErrorCode(),message);
        }catch (SQLDataException e){
            final String message = CommConstants.DATA_ERROR;
            _logger.error(message, e);
            return errorResponse(message);
        }catch (Exception e) {
            final String message = CommConstants.SYSTEM_ERROR;
            _logger.error(message, e);
            return errorResponse(message);
        }
    }

}
