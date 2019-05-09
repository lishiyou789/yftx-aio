package aio.health2world.http.func;


import aio.health2world.http.tool.JsonConvert;
import aio.health2world.http.HttpResult;
import rx.functions.Func1;

/**
 * http://www.jianshu.com/p/93f8c9ae8819
 * Created by _SOLID
 */
public class ResultFunc<T> implements Func1<String, HttpResult<T>> {

    @Override
    public HttpResult<T> call(String result) {

        JsonConvert<HttpResult<T>> convert = new JsonConvert<>();

        return convert.parseData(result);
    }
}
