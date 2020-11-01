package com.xmpp.smackchat.base.repository.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofitBuilder {
    public static Retrofit.Builder defautlBuilder(String baseUrl, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create());
    }

    public static Retrofit.Builder customBuilder() {
        return new Retrofit.Builder();
    }
}
