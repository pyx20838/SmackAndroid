package com.xmpp.smackchat.base.repository.paging;


import androidx.lifecycle.LiveData;


public interface PagingFunc<T> {
    LiveData<PageData<T>> execute(PageParam p);
}
