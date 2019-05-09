package com.konsung.idcard;

public class DecodeWlt {

    static {
        System.loadLibrary("DecodeWlt");
    }

    public native int Wlt2Bmp(String wltPath, String bmpPath);

}
