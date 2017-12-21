package com.tencent.response.model;

import java.util.List;
import java.util.Map;

/**
 * Created by jianghaoming on 2017/12/21.
 *
 *
 + + item	是	string	字段名称
 + + itemstring	是	string	字段识别出来的信息
 + + itemcoord	是	object	字段在图像中的像素坐标，包括左上角坐标x,y，以及宽、高width, height
 + + words	是	array	字段识别出来的每个字的信息
 + + + character	是	string	识别出的单字字符
 + + + confidence	是	float	识别出的单字字符对应的置信度
 */
public class ORCResultModel {

   private String item;
   private String itemstring;
   private List<Map<String,Object>> itemcoord;
   private List<Map<String,Object>> words;

   public String getItem() {
       return item;
   }

   public void setItem(String item) {
       this.item = item;
   }

   public String getItemstring() {
       return itemstring;
   }

   public void setItemstring(String itemstring) {
       this.itemstring = itemstring;
   }


    public List<Map<String, Object>> getItemcoord() {
        return itemcoord;
    }

    public void setItemcoord(List<Map<String, Object>> itemcoord) {
        this.itemcoord = itemcoord;
    }

    public List<Map<String, Object>> getWords() {
        return words;
    }

    public void setWords(List<Map<String, Object>> words) {
        this.words = words;
    }
}
