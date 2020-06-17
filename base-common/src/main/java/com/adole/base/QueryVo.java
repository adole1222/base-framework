package com.adole.base;

import com.github.pagehelper.PageHelper;

public class QueryVo<T> {

    private T query;

    private PageVo page;

    public void resetPage() {
        QueryVo queryVo = this;
        this.resetPage(queryVo);
    }

    public static void resetPage(QueryVo queryVo) {
        if (queryVo == null) {
            queryVo = new QueryVo();
        }
        PageVo page = queryVo.getPage();
        if (page == null) {
            return;
        }
        if (page.getPageNumber() == null) {
            page.setPageNumber(page.getCurrent());
        }
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
    }

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    public PageVo getPage() {
        return page;
    }

    public void setPage(PageVo page) {
        this.page = page;
    }
}
