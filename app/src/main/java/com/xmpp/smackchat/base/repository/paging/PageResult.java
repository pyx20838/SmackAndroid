package com.xmpp.smackchat.base.repository.paging;


import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

public class PageResult<T> {
    private LiveData<PagedList<T>> pageData;
    private LiveData<Integer> total;
    private LiveData<Integer> queryStatus;

    public PageResult(LiveData<PagedList<T>> pageData, LiveData<Integer> total, LiveData<Integer> queryStatus) {
        this.pageData = pageData;
        this.total = total;
        this.queryStatus = queryStatus;
    }

    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public LiveData<Integer> getTotal() {
        return total;
    }

    public LiveData<Integer> getStatus() {
        return queryStatus;
    }
}
