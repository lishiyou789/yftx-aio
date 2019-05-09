package aio.health2world.http.interceptor;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lishiyou on 2017/6/28.
 */

public class NetWorkControlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        int maxAge = 60;
        Request request = chain.request();
        request = request.newBuilder()
                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + maxAge)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
