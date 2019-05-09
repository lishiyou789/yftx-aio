//package aio.health2world.http;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.lang.reflect.Field;
//
//import aio.health2world.SApplication;
//import aio.health2world.http.convert.DoubleDefault0Adapter;
//import aio.health2world.http.convert.FloatDefault0Adapter;
//import aio.health2world.http.convert.IntegerDefault0Adapter;
//import aio.health2world.http.convert.JsonConverterFactory;
//import aio.health2world.http.convert.LongDefault0Adapter;
//import aio.health2world.utils.SPUtils;
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by _SOLID
// */
//public class ServiceFactory {
//
//    private static Gson gson;
//    private OkHttpClient okHttpClient;
//
//    public ServiceFactory() {
//        if (gson == null) {
//            gson = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd hh:mm:ss")
//                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
//                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
//                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
//                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
//                    .registerTypeAdapter(Float.class, new FloatDefault0Adapter())
//                    .registerTypeAdapter(float.class, new FloatDefault0Adapter())
//                    .create();
//        }
//        okHttpClient = OkHttpProvider.getDefaultOkHttpClient();
//    }
//
//    private static class SingletonHolder {
//        private static final ServiceFactory INSTANCE = new ServiceFactory();
//    }
//
//    public static ServiceFactory getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    public static ServiceFactory getCacheInstance() {
//        ServiceFactory factory = SingletonHolder.INSTANCE;
//        factory.okHttpClient = OkHttpProvider.getCacheOkHttpClient();
//        return factory;
//    }
//
//
//    public <S> S createService(Class<S> serviceClass) {
//        String serverUrl = "";
//        try {
//            Field field = serviceClass.getField("BASE_URL");
//            serverUrl = (String) field.get(serviceClass);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.getMessage();
//            e.printStackTrace();
//        }
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(serverUrl)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        return retrofit.create(serviceClass);
//    }
//
//
//}
