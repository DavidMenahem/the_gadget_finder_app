package com.david.thegadjetfinder.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.header("Authorization", "Bearer my-token");
        Request modifiedRequest = builder.build();
        return chain.proceed(modifiedRequest);
    }
}