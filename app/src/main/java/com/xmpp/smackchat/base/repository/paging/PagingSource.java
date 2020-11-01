package com.xmpp.smackchat.base.repository.paging;


import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PageKeyedDataSource;

import com.xmpp.smackchat.base.repository.api.common.ResponseCode;


public class PagingSource<T> extends PageKeyedDataSource<PageParam, T> {

    private PageParam params;
    private PagingFunc<T> pagingFunc;
    private MutableLiveData<Integer> total = new MutableLiveData<>();
    private MediatorLiveData<Integer> status = new MediatorLiveData<>();

    public PagingSource(PageParam params, PagingFunc<T> pagingFunc) {
        this.params = params;
        this.pagingFunc = pagingFunc;
    }

    public MediatorLiveData<Integer> getStatus() {
        return status;
    }

    public MutableLiveData<Integer> getTotal() {
        return total;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<PageParam> loadInitialParams, @NonNull final LoadInitialCallback<PageParam, T> loadInitialCallback) {
        status.postValue(ResponseCode.LOADING);
        status.addSource(pagingFunc.execute(params), new Observer<PageData<T>>() {
            @Override
            public void onChanged(PageData<T> pageData) {
                if (pageData == null) status.postValue(ResponseCode.ERROR);
                else if (pageData.getTotal() <= 0) {
                    status.postValue(ResponseCode.PAGING_EMPTY);
                    total.setValue(0);
                } else {
                    status.postValue(ResponseCode.SUCCESS);
                    total.setValue(pageData.getTotal());
                    loadInitialCallback.onResult(pageData.getItems(), null, params.next());
                }
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<PageParam> loadParams, @NonNull final LoadCallback<PageParam, T> loadCallback) {
        params = params.prev();
        status.addSource(pagingFunc.execute(params), new Observer<PageData<T>>() {
            @Override
            public void onChanged(PageData<T> pageData) {
                if (pageData != null) loadCallback.onResult(pageData.getItems(), params);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<PageParam> loadParams, @NonNull final LoadCallback<PageParam, T> loadCallback) {
        params = params.next();
        status.addSource(pagingFunc.execute(params), new Observer<PageData<T>>() {
            @Override
            public void onChanged(PageData<T> pageData) {
                if (pageData != null) loadCallback.onResult(pageData.getItems(), params);
            }
        });
    }

}
