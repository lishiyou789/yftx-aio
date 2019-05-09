package aio.health2world.http.func;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * http://www.jianshu.com/p/93f8c9ae8819
 * Created by _SOLID
 */
public class StringFunc implements Func1<ResponseBody, String> {

    @Override
    public String call(ResponseBody responseBody) {

        String result = null;

        try {
            result = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
