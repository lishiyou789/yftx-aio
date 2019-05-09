package com.health2world.aio.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.health2world.aio.MyApplication;

import aio.health2world.http.OkHttpProvider;
import aio.health2world.http.convert.DoubleDefault0Adapter;
import aio.health2world.http.convert.FloatDefault0Adapter;
import aio.health2world.http.convert.IntegerDefault0Adapter;
import aio.health2world.http.convert.LongDefault0Adapter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by _SOLID
 */
public class ServiceFactory {

    private static Gson gson;
    private OkHttpClient okHttpClient;

    public ServiceFactory() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd hh:mm:ss")
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Float.class, new FloatDefault0Adapter())
                    .registerTypeAdapter(float.class, new FloatDefault0Adapter())
                    .create();
        }
        okHttpClient = OkHttpProvider.getDefaultOkHttpClient();
    }

    private static class SingletonHolder {
        private static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static ServiceFactory getCacheInstance() {
        ServiceFactory factory = SingletonHolder.INSTANCE;
        factory.okHttpClient = OkHttpProvider.getCacheOkHttpClient();
        return factory;
    }


    public <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getInstance().getServerUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }


}
