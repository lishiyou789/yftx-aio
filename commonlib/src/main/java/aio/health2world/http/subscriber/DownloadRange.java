package aio.health2world.http.subscriber;

/**
 * Created by Administrator on 2018/6/8 0008.
 */

public class DownloadRange {
    long[] start;
    long[] end;

    DownloadRange(long[] start, long[] end) {
        this.start = start;
        this.end = end;
    }
}
