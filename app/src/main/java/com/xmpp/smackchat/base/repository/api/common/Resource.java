package com.xmpp.smackchat.base.repository.api.common;


import androidx.annotation.Nullable;

import java.util.Objects;

public class Resource<T> {

    @ResponseCode
    private int responseCode;

    private T data;

    public Resource(@Nullable T data, @ResponseCode int responseCode) {
        this.data = data;
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public static <T> Resource<T> create(@ResponseCode int responseCode, @Nullable T data) {
        return new Resource<>(data, responseCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;
        if (!Objects.equals(responseCode, resource.responseCode)) {
            return false;
        }
        return Objects.equals(data, resource.data);
    }

    public boolean isEmpty() {
        return responseCode == ResponseCode.PAGING_EMPTY;
    }

    public boolean isSuccess() {
        return responseCode == ResponseCode.SUCCESS && data != null;
    }

    public boolean isError() {
        return responseCode == ResponseCode.ERROR;
    }

    public boolean isLoading() {
        return responseCode == ResponseCode.LOADING;
    }

    public T getData() {
        return data;
    }

    @Override
    public int hashCode() {
        int result = responseCode;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

}
