package aio.health2world.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**添加头部
 * Created by lishiyou on 2017/6/28.
 */

public class HeaderInterceptor implements Interceptor{

    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = builder.addHeader("Content-type", "app/json").build();
        return chain.proceed(request);
    }
}
