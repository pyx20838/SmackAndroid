package com.xmpp.smackchat.base.repository.paging;

public abstract class PageParam {

    protected int page;
    protected int pageSize;

    public PageParam(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public abstract PageParam next();
    public abstract PageParam prev();
}
