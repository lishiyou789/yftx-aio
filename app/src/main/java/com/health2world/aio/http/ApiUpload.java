package com.health2world.aio.http;

import com.health2world.aio.MyApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 文件上传的类
 * Created by lishiyou on 2017/6/30.
 */

public class ApiUpload {

    public static final String URL = "image/upload";

    public static final ApiUpload apiUpload = new ApiUpload();

    public static ApiUpload getInstance() {
        return apiUpload;
    }

    public ApiUpload() {
    }

        /**
         * @param imageType 图片类型
         * @param imagePath 图片本地路径
         * @param callback  回调
         */
        public void uploadImage(String imageType, String imagePath, Callback callback) {
            File file = new File(imagePath);
            String fileName = file.getName();
            RequestBody fileBody = RequestBody.create(MediaType.parse("app/octet-stream"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, fileBody)
                    .addFormDataPart("imageType", imageType)
                    .build();
            Request request = new Request.Builder()
                    .url(MyApplication.getInstance().getServerUrl() + "/api/" + URL)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newBuilder().connectTimeout(15, TimeUnit.SECONDS);
            client.newBuilder().writeTimeout(15,TimeUnit.SECONDS);
            client.newBuilder().readTimeout(15,TimeUnit.SECONDS);
            Call call = client.newCall(request);
            call.enqueue(callback);
    }
}
