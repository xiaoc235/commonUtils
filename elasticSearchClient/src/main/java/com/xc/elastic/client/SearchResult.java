package com.xc.elastic.client;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果集
 * @author jianghaoming
 * @date 2019-07-04 16:50:07
 */
public class SearchResult<T>  {

    public static <T> SearchResult<T> of(List<T> resultList , long totalCount){
        SearchResult<T> searchResult = new SearchResult<>();
        searchResult.setResultList(resultList);
        searchResult.setTotalCount(totalCount);
        return searchResult;
    }

    private List<T> resultList = new ArrayList<>();

    private long totalCount = 0;

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
