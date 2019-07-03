package com.xc.elastic.client;

/**
 * 搜索分页
 * @author jianghaoming
 * @date 2019-07-02 15:13:18
 */
public class SearchPage {
    public static final int DEFAULT_SIZE = 10;
    public static final SearchPage NULL_PAGE = null;
    private int size = DEFAULT_SIZE;
    private int page = 0;

    public static SearchPage of(int page, int size){
        return new SearchPage().setPage(page).setSize(size);
    }

    public static SearchPage of(int page){
        return of(page, DEFAULT_SIZE);
    }

    public int getSize() {
        return size;
    }

    public SearchPage setSize(int size) {
        this.size = size;
        return this;
    }

    public int getPage() {
        return page;
    }

    public SearchPage setPage(int page) {
        this.page = page;
        return this;
    }
}
