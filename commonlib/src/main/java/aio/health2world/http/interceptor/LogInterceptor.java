package aio.health2world.http.interceptor;

import android.util.Log;

import java.io.IOException;
import java.util.Date;

import aio.health2world.Constant;
import aio.health2world.SApplication;
import aio.health2world.utils.DateUtil;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by lishiyou on 2017/6/28.
 */

public class LogInterceptor implements Interceptor {

    public static final String TAG = LogInterceptor.class.getSimpleName();

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (SApplication.isDebug)
            Log.i(TAG, "url==" + request.url());
        long startTime = System.currentTimeMillis();
//        String time = DateUtil.getCurrentTime(new Date(System.nanoTime()));
//        if (SApplication.isDebug)
//            Log.e(TAG, "-----------------------start:" + time + "------------------------------");
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        if (SApplication.isDebug)
            Log.e(TAG, "------------------------end:" + duration + "毫秒------------------------");
        if (SApplication.isDebug)
            Log.i(TAG, "result==" + content + " \n \n ");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}