package com.xmpp.smackchat.base.repository;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.xmpp.smackchat.base.repository.paging.PageParam;
import com.xmpp.smackchat.base.repository.paging.PageResult;
import com.xmpp.smackchat.base.repository.paging.PagingSource;
import com.xmpp.smackchat.base.thread.ThreadManager;


public class PagingQuery<T> {

    private PageResult<T> pageResult;

    public PagingQuery(final PagingSource<T> pagingSource) {
        DataSource.Factory<PageParam, T> pagingFactory = new DataSource.Factory<PageParam, T>() {
            @Override
            public DataSource<PageParam, T> create() {
                return pagingSource;
            }
        };
        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(3)
                .build();
        LiveData<PagedList<T>> pagedData = new LivePagedListBuilder<>(pagingFactory, pageConfig)
                .setFetchExecutor(ThreadManager.getInstance().getNetworkPool())
                .build();
        this.pageResult = new PageResult<>(pagedData, pagingSource.getTotal(), pagingSource.getStatus());
    }

    public PageResult<T> getResult() {
        return pageResult;
    }
}
