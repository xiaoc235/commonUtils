package com.xc.elastic.client;

import com.common.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElasticClient {


    private static RestHighLevelClient restHighLevelClient;
    private static ElasticClient elasticClient;


    public static ElasticClient create(HttpHost... httpHost){
        elasticClient = new ElasticClient();
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        return elasticClient;
    }

    public RestHighLevelClient getRestClient(){
        if(elasticClient == null){
            throw new RuntimeException("elasticClient is not init");
        }
        return restHighLevelClient;
    }

    /**
     * 新增索引
     * @author jianghaoming
     * @date 2019-07-02 17:24:52
     */
    public void createIndex(ElasticEntity entity, String json) throws IOException {
        IndexRequest request = new IndexRequest(entity.getIndexName());
        request.id(entity.getId());
        request.source(json, XContentType.JSON);
        getRestClient().index(request, RequestOptions.DEFAULT);
    }

    public void createIndex(ElasticEntity entity, Map<String,Object> map) throws IOException {
        this.createIndex(entity,GsonUtils.toJson(map));
    }

    /**
     * 获取索引
     * @param entity
     * @return
     * @throws IOException
     */
    public String get(ElasticEntity entity) throws IOException {
        GetRequest request = new GetRequest(entity.getIndexName());
        request.id(entity.getId());
        GetResponse response = getRestClient().get(request, RequestOptions.DEFAULT);
        String result =  response.getSourceAsString();
        return (null == result || ("").equals(result) || ("null").equals(result)) ? "" : result ;
    }
    public <T> T get(ElasticEntity entity, Class<T> cls) throws IOException {
        String result = get(entity);
        return ("").equals(result) ? null : GsonUtils.convertObj(result, cls);
    }


    /**
     * 修改索引
     * @param entity
     * @param json
     * @throws IOException
     */
    public void update(ElasticEntity entity, String json) throws IOException {
        if("".equals(this.get(entity))) {
            throw new RuntimeException("the " + entity.getId() + " not exist");
        }
        UpdateRequest request = new UpdateRequest(entity.getIndexName(), entity.getId());
        request.doc(json, XContentType.JSON);
        getRestClient().update(request, RequestOptions.DEFAULT);
    }
    public void update(ElasticEntity entity, Map<String,Object> map) throws IOException {
        this.update(entity, GsonUtils.toJson(map));
    }

    /**
     * 删除
     * @author jianghaoming
     * @date 2019-07-02 17:29:54
     */
    public void delete(ElasticEntity entity) throws IOException{
        DeleteRequest request = new DeleteRequest(entity.getIndexName());
        request.id(entity.getId());
        getRestClient().delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 搜索
     * @author jianghaoming
     * @date 2019-07-02 17:41:54
     * @throws IOException
     */
    public SearchResult<String> search(SearchRequest searchRequest) throws IOException {
        SearchResponse response = getRestClient().search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = response.getHits().getHits();
        List<String> resultList = new ArrayList<>();
        for(SearchHit searchHit : searchHits){
            Map<String, HighlightField> highlightFieldMap = searchHit.getHighlightFields();
            if(highlightFieldMap.isEmpty()) {
                resultList.add(searchHit.getSourceAsString());
            }else{
                Map<String, Object> sourceMap = searchHit.getSourceAsMap();
                //处理字段高亮
                highlightFieldMap.forEach((filedKey, filedValue) -> {
                    StringBuilder textBuilder = new StringBuilder();
                    for(Text text : filedValue.getFragments()){
                        textBuilder.append(text);
                    }
                    sourceMap.put(filedKey, textBuilder);
                });
                resultList.add(GsonUtils.anotherToJson(sourceMap));
            }
        }
        return SearchResult.of(resultList, response.getHits().getTotalHits().value);
    }

    public <T> SearchResult<T> search(SearchRequest searchRequest, TypeToken<List<T>> typeToken) throws IOException {
        SearchResult<String> jsonList = this.search(searchRequest);
        List<T> resultList = new ArrayList<>();
        if(jsonList == null || jsonList.getTotalCount() == 0){
            return SearchResult.of(resultList, 0);
        }
        resultList = GsonUtils.convertList(jsonList.getResultList().toString(), typeToken);
        return SearchResult.of(resultList, jsonList.getTotalCount());
    }

}
