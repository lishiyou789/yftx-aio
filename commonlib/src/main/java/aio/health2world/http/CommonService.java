package aio.health2world.http;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by _SOLID
 */
public interface CommonService {

    String BASE_URL = "http://";//这个不重要，可以随便写，但是必须有

    @GET
    Observable<ResponseBody> loadString(@Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
