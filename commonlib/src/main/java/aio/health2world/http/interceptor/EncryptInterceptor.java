//package aio.yftx.library.http.interceptor;
//
///**
// * Created by lishiyou on 2017/6/28.
// */
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import aio.yftx.library.Constant;
//import aio.yftx.library.utils.MD5Util;
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * 对所有参数进行排序加密得到sign值 作为请求链接后面的参数值
// */
//public class EncryptInterceptor implements Interceptor {
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        HttpUrl httpUrl = request.url();
//        Set<String> set = httpUrl.queryParameterNames();
//        Map<String, String> map = new HashMap<>();
//        for (String s : set) {
//            map.put(s, httpUrl.queryParameter(s));
//        }
//        // 通过ArrayList构造函数把map.entrySet()转换成list
//        List<Map.Entry<String, String>> mapList = new ArrayList<>(map.entrySet());
//        // 通过比较器实现比较排序
//        Collections.sort(mapList, new Comparator<Map.Entry<String, String>>() {
//            public int compare(Map.Entry<String, String> mapping1,
//                               Map.Entry<String, String> mapping2) {
//                return mapping1.getKey().compareTo(mapping2.getKey());
//            }
//        });
//        StringBuffer buffer = new StringBuffer();
//        for (Map.Entry<String, String> mapping : mapList) {
//            buffer.append(mapping.getKey() + "=" + mapping.getValue());
//        }
//        String sign = buffer.append(Constant.APPSECRET_VALUES).toString();
//        HttpUrl newHttpUrl = request.url().newBuilder()
//                .addQueryParameter("sign", MD5Util.getMD5String(sign))
//                .build();
//        request = request.newBuilder().url(newHttpUrl).build();
//        return chain.proceed(request);
//    }
//}
