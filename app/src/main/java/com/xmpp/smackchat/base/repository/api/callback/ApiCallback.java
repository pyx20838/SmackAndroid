package com.xmpp.smackchat.base.repository.api.callback;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.xmpp.smackchat.base.AppLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallback<T> implements Callback<T> {

    private MutableLiveData<T> data;

    public ApiCallback() {
        this.data = new MutableLiveData<>();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            AppLog.i("Response success: " + response);
            data.setValue(response.body());
        } else {
            AppLog.e("Response failed with message: " + response.message() + ", Response: " + response + ", Error: " + response.errorBody());
            data.setValue(null);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (data != null) {
            AppLog.e("Throwable: " + throwable + ", message: " + throwable.getMessage());
            data.setValue(null);
        }
    }

    public LiveData<T> asLiveData() {
        return data;
    }
}
