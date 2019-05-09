package aio.health2world.http.interceptor;

import android.util.Log;

import com.google.gson.Gson;
import com.konsung.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aio.health2world.Constant;
import aio.health2world.SApplication;
import aio.health2world.utils.MD5Util;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 对参数加密生成sign的值放入请求链接的后面
 * Created by lishiyou on 2017/6/28.
 */

public class UrlRedirectInterceptor implements Interceptor {

    private static final String TAG = UrlRedirectInterceptor.class.getSimpleName();

    public UrlRedirectInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        //buffer流
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        String paramsJson = buffer.readUtf8();
        //获取参数
        HashMap<String, Object> rootMap = new Gson().fromJson(paramsJson, HashMap.class);
        // 通过ArrayList构造函数把map.entrySet()转换成list
        List<Map.Entry<String, Object>> mapList = new ArrayList<>(rootMap.entrySet());
        // 通过比较器实现比较排序
        Collections.sort(mapList, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> mapping1,
                               Map.Entry<String, Object> mapping2) {
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> mapping : mapList) {
            sb.append(mapping.getKey() + "=" + mapping.getValue());
            if (SApplication.isDebug)
                Log.i(TAG, mapping.getKey() + "=" + mapping.getValue());
        }
        if (SApplication.isDebug)
            Log.i(TAG, "request==" + paramsJson);
        String sign = sb.append(Constant.APPSECRET_VALUES).toString();
        HttpUrl newHttpUrl = request.url().newBuilder()
                .addQueryParameter("sign", MD5Util.getMD5String(sign))
                .build();
        request = request.newBuilder().url(newHttpUrl).build();
        return chain.proceed(request);
    }
}
