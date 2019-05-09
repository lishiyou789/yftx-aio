package com.konsung.idcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class Wlt2Bmp {
    public static Bitmap getBmpFromByteArray(byte[] photo) {
        try {
            File wltFile = new File("/sdcard/photo.wlt");
            FileOutputStream fos = new FileOutputStream(wltFile);
            fos.write(photo, 0, photo.length);
            fos.close();

            DecodeWlt dw = new DecodeWlt();
            int result = dw.Wlt2Bmp("/sdcard/photo.wlt", "/sdcard/photo.bmp");
            if (result == 1) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 1;
                opt.inJustDecodeBounds = false;
                //BMP图片数据
                Bitmap bmp = BitmapFactory.decodeFile("/sdcard/photo.bmp",
                        opt);
                if (bmp != null) {
                    return bmp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
