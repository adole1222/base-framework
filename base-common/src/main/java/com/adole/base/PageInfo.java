package com.adole.base;

import com.github.pagehelper.Page;

import java.util.List;

public class PageInfo<T> {

    private Integer pageNumber;

    private Integer pageSize;

    private Long total;

    private Integer totalPage;

    private List<T> list;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static PageInfo getPageInfo(Page page) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.list = page.getResult();
        pageInfo.pageNumber = page.getPageNum();
        pageInfo.pageSize = page.getPageSize();
        pageInfo.total = page.getTotal();
        pageInfo.totalPage = page.getPages();
        return pageInfo;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
