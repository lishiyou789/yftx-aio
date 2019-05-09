package aio.health2world.http;

import java.io.File;
import java.util.concurrent.TimeUnit;

import aio.health2world.http.interceptor.CacheControlInterceptor;
import aio.health2world.http.interceptor.HeaderInterceptor;
import aio.health2world.http.interceptor.LogInterceptor;
import aio.health2world.http.interceptor.ParamsInterceptor;
import aio.health2world.http.interceptor.UrlRedirectInterceptor;
import aio.health2world.SApplication;
import aio.health2world.http.interceptor.NetWorkControlInterceptor;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by _SOLID
 */

public class OkHttpProvider {
    private static final int DEFAULT_TIME_OUT = 30;
    private static final int DEFAULT_READ_TIME_OUT = 30;

    public static OkHttpClient getDefaultOkHttpClient() {
        return getNoCacheOkHttpClient(new NetWorkControlInterceptor());
    }

    public static OkHttpClient getCacheOkHttpClient() {
        return getCacheOkHttpClient(new CacheControlInterceptor());
    }

    private static OkHttpClient getCacheOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时时间
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new ParamsInterceptor());
        builder.addInterceptor(new UrlRedirectInterceptor());
        builder.addNetworkInterceptor(cacheControl);
        builder.addInterceptor(new LogInterceptor());
        //设置缓存
        File httpCacheDirectory = new File(SApplication.mInstance.getCacheDir(), "OkHttpCache");
        builder.cache(new Cache(httpCacheDirectory, 100 * 1024 * 1024));
        return builder.build();
    }

    private static OkHttpClient getNoCacheOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        //设置超时时间
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        //设置拦截器(这里要注意拦截器的顺序)
        builder.addInterceptor(new HeaderInterceptor());
        builder.addInterceptor(new ParamsInterceptor());
        builder.addInterceptor(new UrlRedirectInterceptor());
        builder.addNetworkInterceptor(cacheControl);
        builder.addInterceptor(new LogInterceptor());
        return builder.build();
    }
}
