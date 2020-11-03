package com.xmpp.smackchat.base.repository;


import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.xmpp.smackchat.base.repository.api.common.Resource;
import com.xmpp.smackchat.base.repository.api.common.ResponseCode;

import java.util.Objects;

public abstract class RepoQuery<Response> {

    private MediatorLiveData<Resource<Response>> data = new MediatorLiveData<>();

    public LiveData<Resource<Response>> asLiveData() {
        postValue(ResponseCode.LOADING, null);
        data.addSource(query(), new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                postValue(response == null ? ResponseCode.ERROR : ResponseCode.SUCCESS, response);
            }
        });
        return data;
    }

    protected abstract LiveData<Response> query();

    @MainThread
    private void postValue(@ResponseCode int responseCode, Response resultData) {
        Resource<Response> newValue = Resource.create(responseCode, resultData);
        if (!Objects.equals(data.getValue(), newValue)) {
            data.postValue(newValue);
        }
    }

    public static <T> LiveData<Resource<T>> queryChains(final int index, final RepoQuery<T>... queries) {
        final MutableLiveData<Resource<T>> finalResult = new MutableLiveData<>();
        if (index >= queries.length) {
            finalResult.postValue(Resource.create(ResponseCode.ERROR, (T) null));
            return finalResult;
        }

        RepoQuery<T> query = queries[index];
        return Transformations.switchMap(query.query(), new Function<T, LiveData<Resource<T>>>() {
            @Override
            public LiveData<Resource<T>> apply(T firstResult) {
                if (firstResult == null) return queryChains(index + 1, queries);
                finalResult.postValue(Resource.create(ResponseCode.SUCCESS, firstResult));
                return finalResult;
            }
        });
    }

    public static LiveData<Resource<Object>> queryChainsByObj(final int index, final RepoQuery<Object>... queries) {
        final MutableLiveData<Resource<Object>> finalResult = new MutableLiveData<>();
        if (index >= queries.length) {
            finalResult.postValue(Resource.create(ResponseCode.ERROR, null));
            return finalResult;
        }

        RepoQuery<Object> query = queries[index];
        return Transformations.switchMap(query.query(), new Function<Object, LiveData<Resource<Object>>>() {
            @Override
            public LiveData<Resource<Object>> apply(Object firstResult) {
                if (firstResult == null) return queryChainsByObj(index + 1, queries);
                finalResult.postValue(Resource.create(ResponseCode.SUCCESS, firstResult));
                return finalResult;
            }
        });
    }
}