package com.xc.elastic.client;

import com.common.base.BaseEntity;


public class ElasticEntity extends BaseEntity {

    private String indexName;
    private String id;

    public static ElasticEntity builder(){
        return new ElasticEntity();
    }

    public static ElasticEntity of(String indexName, String id){
        return builder().setId(id).setIndexName(indexName);
    }

    public String getIndexName() {
        return indexName;
    }

    public ElasticEntity setIndexName(String indexName) {
        this.indexName = indexName;
        return this;

    }


    public String getId() {
        return id;
    }

    public ElasticEntity setId(String id) {
        this.id = id;
        return this;
    }
    
}
