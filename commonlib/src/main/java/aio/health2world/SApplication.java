package aio.health2world;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:20:59
 */
public class SApplication extends Application {

    public static Context mInstance;

    public static boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static void init(Context context, boolean debug) {
        mInstance = context;
        isDebug = debug;
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }
}
