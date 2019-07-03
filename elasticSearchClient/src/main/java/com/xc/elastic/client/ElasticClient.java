package com.xc.elastic.client;

import com.common.utils.GsonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

    public static ElasticClient create(String hostname, int port){
        elasticClient = new ElasticClient();
        HttpHost httpHost = new HttpHost(hostname,port);
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
     * @param nameFiled 需要搜索的字段名
     * @param keyWord 搜索关键字
     * @param indexName 索引名称
     * @param page 分页
     * @return
     * @throws IOException
     */
    public List<String> search(String[] nameFiled, String keyWord, String[] indexName, SearchPage page) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyWord,nameFiled)
                .fuzziness(Fuzziness.ZERO);
        sourceBuilder.query(queryBuilder);
        if(page != null) {
            sourceBuilder.from(page.getPage() * page.getSize());
            sourceBuilder.size(page.getSize());
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span name='els-result' class='els-result'>");
        highlightBuilder.postTags("</span>");
        for(String filed : nameFiled){
            highlightBuilder.fields().add(new HighlightBuilder.Field(filed));
        }
        sourceBuilder.highlighter(highlightBuilder);
        request.source(sourceBuilder);
        SearchResponse response = getRestClient().search(request, RequestOptions.DEFAULT);
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
        return resultList;
    }

    public <T> List<T> search(String[] nameFiled, String key, String[] indexName, SearchPage page, TypeToken<List<T>> typeToken) throws IOException {
        List<String> jsonList = this.search(nameFiled, key, indexName, page);
        List<T> resultList = new ArrayList<>();
        if(jsonList == null || jsonList.isEmpty()){
            return resultList;
        }
        resultList = GsonUtils.convertList(jsonList.toString(), typeToken);
        return resultList;
    }

}
