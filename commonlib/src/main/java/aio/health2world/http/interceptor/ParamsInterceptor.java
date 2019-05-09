package aio.health2world.http.interceptor;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import aio.health2world.SApplication;
import aio.health2world.utils.AppUtils;
import aio.health2world.utils.DeviceUtil;
import aio.health2world.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 加入系统参数的拦截器
 * Created by lishiyou on 2017/6/28.
 */

public class ParamsInterceptor implements Interceptor {

    public ParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        //buffer流
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        String paramsJson = buffer.readUtf8();
        //获取原始参数
        HashMap<String, Object> rootMap = new Gson().fromJson(paramsJson, HashMap.class);

        //添加公共参数
        rootMap.put("appKey", "888888");//固定
        rootMap.put("appType", "1");//1:一体机 2:医生端 3:患者端
        rootMap.put("appVersion", AppUtils.getAppVersionName(SApplication.mInstance));//app版本号
        rootMap.put("versionNo", AppUtils.getAppVersionCode(SApplication.mInstance) + "");
        rootMap.put("osVersion", android.os.Build.VERSION.RELEASE);//操作系统版本号 android 6.0
        rootMap.put("phoneModel", android.os.Build.MODEL);//手机型号
        rootMap.put("sysType", "2");//设备类型  1 ios  2 android
        rootMap.put("timestamp", System.currentTimeMillis() + "");//时间戳
        //设备编号 这里传androidId
        rootMap.put("deviceNo", DeviceUtil.getAndroidId(SApplication.mInstance));
        //设备Token 设备铭牌上的编号
        rootMap.put("deviceToken", AppUtils.getDeviceToken());
        request = request.newBuilder()
                .post(RequestBody.create(requestBody.contentType(), new Gson().toJson(rootMap)))
                .build();
        return chain.proceed(request);
    }
}
