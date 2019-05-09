package aio.health2world.http;

import java.io.Serializable;

/**
 * 服务端数据返回模型
 * Created lishiyou
 */
public class HttpResult<T> implements Serializable {

    public String code;
    public String errorMessage;
    public T data;


    @Override
    public String toString() {
        return "HttpResult{" +
                "code='" + code + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data +
                '}';
    }

}
