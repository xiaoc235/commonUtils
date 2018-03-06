package com.xc.wechat.utils;

import com.common.base.exception.BusinessException;
import com.common.utils.CommonUtils;
import com.xc.wechat.config.ConfigKey;

/**
 * 基本检查类
 */
public class CheckUtil {

    public static void checkAppIdAndMchId() throws BusinessException{
        if(CommonUtils.isBlank(ConfigKey.APPID) || CommonUtils.isBlank(ConfigKey.MCHID)){
            throw new BusinessException("appid or mchis is null !");
        }
    }
}
