package aio.health2world.http.subscriber;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2018/6/8 0008.
 */

public interface DownloadApi {
    @GET
    @Streaming
    Observable<Response<ResponseBody>> download(@Header("Range") String range, @Url String url);

    @HEAD
    Observable<Response<Void>> getHttpHeader(@Header("Range") String range, @Url String url);

    @HEAD
    Observable<Response<Void>> getHttpHeaderWithIfRange(@Header("Range") final String range,
                                                        @Header("If-Range") final String lastModify,
                                                        @Url String url);
}
