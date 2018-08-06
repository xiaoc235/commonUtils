package com.common.dao;

import com.common.base.model.MyPageResult;
import com.common.utils.CommonUtils;
import com.common.utils.DateFormatUtil;
import com.common.utils.GsonUtils;
import com.common.utils.ReflectionUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by jianghaoming on 2017/3/1523:08.
 */

@Transactional
@Repository
@Qualifier("BaseDao")
public abstract class BaseDaoImpl<T>{


    private static final Logger _logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    private static final String LIMIT_SQL = " limit ";
    private static final String ORDER_BY_SQL = " order by ";


    @PersistenceContext(unitName = "entityManagerFactory")
    EntityManager entityManager;


    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * * 查询数据集合,不分页
     */
    protected List<T> queryListEntity(String sql, Map<String, Object> params, Class<T> clazz){
        //转换 --> map 转 json ,  json to List.Object
        List<T> resultList = null;
        try {
            resultList = this.queryList(sql,params);
            if(resultList.isEmpty()){
                return new ArrayList<>();
            }
            JsonArray jsonList = new JsonArray();
            for(T t : resultList){
                Map<String,Object> map = (Map<String, Object>) t;
                JsonObject jsonObject = convertJsonObject(map);
                jsonList.add(jsonObject);
            }
            resultList = GsonUtils.convertList(jsonList.toString(), clazz);
        }catch (Exception e){
            _logger.error(e.getMessage(),e);
            resultList = new ArrayList();
        }
        return resultList;
    }


    /**
     * 查询单条数据
     * 通过list来转换
     */
    protected T queryEntity(String sql, Map<String, Object> params, Class<T> clazz){
       List<Map<String,Object>> resultList = (List<Map<String,Object>>) this.queryList(sql,params);
       if(!resultList.isEmpty()){
           return GsonUtils.convertObj(convertJsonObject(resultList.get(0)).toString(),clazz);
       }else{
           return null;
       }
    }



    /**
     * 查询数据集合，分页
     */
    protected MyPageResult<T> queryListEntityByPage(String sql, Map<String, Object> params, Class clazz, Pageable pageable){
        MyPageResult<T> pageResult = new MyPageResult<>();
        if(null == pageable){
            List<T> resultList = this.queryListEntity(sql,params,clazz);
            pageResult.setResultList(resultList);
            pageResult.setTotalCount(resultList.size());
            return pageResult;
        }

        int pageNumber = pageable.getPageNumber();
        if(pageNumber<0){
            pageNumber = 0;
        }
        final int pageSize = pageable.getPageSize();

        //排序sql拼接 order by
        StringBuilder pageSql = new StringBuilder(sql);
        Sort sort = pageable.getSort();
        if(sort!=null) {
            pageSql.append(ORDER_BY_SQL);
            for (Sort.Order order : sort) {
                pageSql.append(CommonUtils.underscoreName(order.getProperty()) + " " + order.getDirection());
            }
        }


        //分页sql拼接 limit
        pageSql.append(LIMIT_SQL);
        pageSql.append(pageNumber*pageSize+","+pageSize);


        String querySql = pageSql.toString();
        List<T> resultList = this.queryListEntity(querySql,params,clazz);
        pageResult.setResultList(resultList);

        final int totalCount = this.getCountBy("select count(1) from ("+sql+") t",params);
        pageResult.setTotalCount(totalCount);

        int totalPageNum = pageSize == 0 ? 1 : (int) Math.ceil((double) totalCount / (double) pageSize);
        pageResult.setTotalPageNumber(totalPageNum);

        return pageResult;
    }




    /**
     * 获取记录条数
     * @param sql
     * @param params
     * @return
     */
    protected Integer getCountBy(String sql,Map<String, Object> params){
        BigInteger bigInteger  = (BigInteger) this.query(sql,params).getSingleResult();
        return bigInteger.intValue();
    }

    /**
     *  查询单个值
     * @param sql
     * @param params
     * @return
     */
    protected Map<String,Object> getSingleResult(String sql,Map<String, Object> params){
        List<T> list = this.queryList(sql,params);
        if(!list.isEmpty()){
            return (Map<String, Object>) list.get(0);
        }
        return null;
    }

    /**
     * 新增或者删除
     * @param sql
     * @param params
     * @return
     */
    protected Integer execute(String sql,Map<String, Object> params){
        return this.query(sql,params).executeUpdate();
    }


    /**
     * 批量添加
     * 解决大数据量添加下，hibernate效率问题
     * @param ts
     * @return
     */
    public List<T> saveList(List<T> ts){
        List<T> result = new ArrayList<>();
        final String _insert_sql_str = "insert into ";
        try {

            StringBuilder sql = new StringBuilder(_insert_sql_str); //运行的sql
            StringBuilder insertField = new StringBuilder(); //需要保存的字段名
            StringBuilder insertValue = new StringBuilder(); //插入的value
            Map<Integer,Object> paraMap = new HashMap<>(); //参数
            String tableName = ""; //表名
            int valueIndex = 0;
            //处理entity
            for (T entity : ts) {
                Field fields[] = ReflectionUtils.getDeclaredField(entity);
                insertValue.append("(");
                //获取字段列表
                for (Field fie : fields) {
                    String fieldName = fie.getName(); //字段名
                    if (!fieldName.equals("serialVersionUID")) {
                        if(result.isEmpty()) {
                            for (Annotation anno : entity.getClass().getAnnotations()){
                                if(anno.annotationType().equals(Entity.class)){
                                    tableName = ((Entity)anno).name();
                                    if(!CommonUtils.isBlank(tableName)){
                                        break;
                                    }
                                }
                                if(anno.annotationType().equals(Table.class)){
                                    tableName = ((Table)anno).name();
                                    break;
                                }
                            }
                            insertField.append(CommonUtils.underscoreName(fieldName) + ",");
                        }
                        Object fieldValue = ReflectionUtils.getFieldValue(entity, fieldName);
                        //判断自主增加的uuid , createTime, updateTime
                        if(fie.getAnnotations() !=null && fie.getAnnotations().length>1){
                            for(Annotation anno : fie.getAnnotations()){
                                if(anno.annotationType().equals(GenericGenerator.class)){
                                    fieldValue = UUID.randomUUID().toString();
                                    break;
                                }
                                if(anno.annotationType().equals(CreationTimestamp.class)
                                        || anno.annotationType().equals(UpdateTimestamp.class) ){

                                    String pattern = DateFormatUtil.YYYY_MM_DD_HH_MM_SS;
                                    if((fie.getAnnotation(DateTimeFormat.class)) != null){
                                        pattern = (fie.getAnnotation(DateTimeFormat.class)).pattern();
                                    }

                                    if(fieldValue != null){
                                        fieldValue = DateFormatUtil.dateToString((Date) fieldValue,pattern);
                                    }else {
                                        fieldValue = DateFormatUtil.getCurrentTime(pattern);
                                    }
                                    break;
                                }
                            }
                        }
                        valueIndex++;
                        paraMap.put(valueIndex,fieldValue);
                        insertValue.append("?,");
                    }
                }
                insertValue = new StringBuilder(this.substring(insertValue) +"),");
                result.add(entity);

                //处理大数量 , 分批插入
                if(result.size() >= 500){
                    sql.append(tableName);
                    sql.append("("+this.substring(insertField)+") values");
                    sql.append(this.substring(insertValue));
                    this.executeUpdate(sql.toString(),paraMap);

                    result = new ArrayList<>();
                    sql = new StringBuilder(_insert_sql_str); //运行的sql
                    insertField = new StringBuilder(); //需要保存的字段名
                    insertValue = new StringBuilder(); //插入的value
                    paraMap = new HashMap<>();
                    valueIndex = 0;
                }
            }


            if(result.isEmpty()) {
                sql.append(tableName);
                sql.append("(" + this.substring(insertField) + ") values");
                sql.append(this.substring(insertValue));
                this.executeUpdate(sql.toString(), paraMap);
            }

        }catch (Exception e){
            _logger.error(e.getMessage(),e);
        }

        return result;

    }







    private Query query(final String sql,  Map<String, Object> params){
        Query query =  entityManager.createNativeQuery(sql);
        _logger.info("sql :{} ", sql);
        if (params != null) {
            StringBuilder paramStr = new StringBuilder();

            for(Map.Entry<String,Object> entry : params.entrySet()){
                paramStr.append(entry.getKey()+"="+entry.getValue()+" ");
                query.setParameter(entry.getKey(), entry.getValue());
            }
            _logger.info("params [{}]",paramStr);
        }
        return query;
    }

    private SQLQuery getSqlQuery(final String sql,  Map<String, Object> params){
        Session session = entityManager.unwrap(Session.class);
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        _logger.info("sql :{} ", sql);
        if (params != null) {
            StringBuilder paramStr = new StringBuilder();
            for(Map.Entry<String,Object> entry : params.entrySet()){
                paramStr.append(entry.getKey()+"="+ entry.getValue()+" ");
                query.setParameter(entry.getKey(), entry.getValue());
            }
            _logger.info("params :{}",paramStr);
        }
        return query;
    }


    private List<T> queryList(String sql, Map<String, Object> params){
        List<T> resultList = this.getSqlQuery(sql,params).list();
        if(resultList == null){
            resultList = new ArrayList<>();
        }
        return resultList;
    }


    /**
     * map 去除下划线，并转换为jsonObject
     * @user jianghaoming 
     * @date 2017/11/13  下午4:45
     *
     */
    private JsonObject convertJsonObject(Map<String,Object> map){
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            if(entry.getValue()!=null) {
                String key = CommonUtils.camelName(entry.getKey());
                String classTypeName = entry.getValue().getClass().getTypeName();
                if(classTypeName.equals("java.lang.Integer")){
                    jsonObject.addProperty(key, Integer.parseInt(entry.getValue()+""));
                }else if(classTypeName.equals("java.sql.Timestamp")){
                    Timestamp timestamp = (Timestamp) entry.getValue();
                    jsonObject.addProperty(key,timestamp.getTime());
                }
                else{
                    jsonObject.addProperty(key, entry.getValue()+"");
                }
            }
        }
        return jsonObject;
    }



    private String substring(StringBuilder str){
        return str.substring(0,str.length()-1);
    }

    private int executeUpdate(String sql, Map<Integer,Object> paraMap){
        Query query = entityManager.createNativeQuery(sql);
        for(Map.Entry<Integer,Object> entry : paraMap.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        int result = query.executeUpdate();
        //清理缓存
        entityManager.flush();
        entityManager.clear();
        return result;

    }
}

