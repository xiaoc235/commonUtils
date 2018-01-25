package com.tencent.api;

import com.common.utils.Base64Util;
import com.common.utils.GsonUtils;
import com.common.utils.HttpClientUtils;
import com.tencent.base.APIServices;
import com.tencent.config.InitConfig;
import com.tencent.response.model.BaseTencentResultModel;
import com.tencent.response.model.ORCResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片OCR识别
 * Created by jianghaoming on 2017/12/21.
 */
public class OrcAPI extends APIServices{

    private static Logger _logger = LoggerFactory.getLogger(OrcAPI.class);

    private static final String apiUrl = ORCUrl.commonORC;

    /**
     * 通用orc识别方法
     * @param imgBase64Str 图片base编码
     * @throws Exception
     */
    public static List<ORCResultModel> commonOrc(String imgBase64Str) throws Exception {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("image",imgBase64Str);
        paramMap = buildParamMap(paramMap);
        String resultJson = HttpClientUtils.post(apiUrl,paramMap);
        //System.out.println(resultJson);
        BaseTencentResultModel resultModel = GsonUtils.convertObj(resultJson,BaseTencentResultModel.class);

        if(resultModel.getRet().equals("0")) {
            Object obj = resultModel.getData().get("item_list");
            String objJson = GsonUtils.toJson(obj);
            List<ORCResultModel> result = GsonUtils.convertList(objJson,ORCResultModel.class);
            return result;
        }else{
            _logger.info("tencent orc error , msg : {}", resultJson);
        }
        return null;
    }

    public static List<ORCResultModel> commonOrc(File file) throws Exception {
        String imgBase64Str = Base64Util.getImageStr(file);
        return commonOrc(imgBase64Str);
    }



    public static void main(String[] args) throws Exception {
        InitConfig.init("1106551225","ATJZTWoDgPeEQQdK");
        String base64Str = Base64Util.getImageStr("/Users/jacky/Documents/1111.png");
        //System.out.println(base64Str);
        List<ORCResultModel> list = commonOrc(base64Str);
        for(ORCResultModel item : list){
            System.out.println(item.getItemstring());
        }
    }
}
