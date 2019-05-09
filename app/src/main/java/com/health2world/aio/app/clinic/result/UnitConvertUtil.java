package com.health2world.aio.app.clinic.result;

import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位转换工具类
 * @author ouyangfan
 * @version 0.0.1
 */
public class UnitConvertUtil {

    private static final int FOUR_NUMBER = 4;
    private static final int FACTOR_FF = 0xFF;
    private static final int FORTY_NINETYSIX_NUMBER = 4096;

    /**
     * 私有构造
     */
    private UnitConvertUtil() {

    }

    /**
     * 转换波形数据
     * @param value 波形数据
     * @return 转换后的数据
     * @throws Exception 转换异常
     */
    public static int[] intValue(String value) {
        byte[] out = Base64.decode(value.getBytes(), Base64.DEFAULT);
        byte[] f = new byte[4];
        f[3] = out[0];
        f[2] = out[1];
        f[1] = out[2];
        f[0] = out[3];
        List<Integer> vList = new ArrayList<>();
        for (int i = 4; i < out.length; i++) {
            f = new byte[2];
            f[1] = out[i];
            f[0] = out[i + 1];
            vList.add(toInt(f));
            i++;
        }
        int[] b = new int[vList.size()];
        for (int i = 0; i < vList.size(); i++) {
            b[i] = vList.get(i);
        }
        return b;
    }

    /**
     * 字节转int
     * @param bRefArr 字节数据
     * @return int
     */
    private static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;
        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & FACTOR_FF) << (8 * i);
        }
        return iOutcome;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            int value = charToByte(hexChars[pos]) << FOUR_NUMBER | charToByte(hexChars[pos + 1]);
            d[i] = (byte) value;
        }
        return d;
    }

    /**
     * getByteformHexString
     *
     * @param hexString hexString
     * @return getByteformHexString
     */
    public static byte[] getByteformHexString(String hexString) {
        //去最高位
        byte[] bytes = hexStringToBytes(hexString);
        return changeByte(bytes);
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * changeByte
     *
     * @param a a
     * @return changeByte
     */
    public static byte[] changeByte(byte[] a) {
        List<Integer> iList = new ArrayList<>();
        int len = 0;
        int aLength = 0;
        if (a != null) {
            aLength = a.length;
        } else {
            aLength = 0;
        }
        if (aLength % 2 == 1) {
            len = 1;
        }
        for (int i = FOUR_NUMBER; i < aLength - len; i++) {
            byte[] f = new byte[2];
            f[0] = a[i];
            f[1] = a[i + 1];
            int v = toInt(f);
            if (v > FORTY_NINETYSIX_NUMBER) {
                v = v % FORTY_NINETYSIX_NUMBER;
            }
            iList.add(v);
            i++;
        }

        byte[] d = new byte[4 + iList.size() * 2];
        byte[] fb = toByteArray(iList.size(), 4);
        d[0] = fb[3];
        d[1] = fb[2];
        d[2] = fb[1];
        d[3] = fb[0];
        for (int i = 0; i < iList.size(); i++) {
            fb = toByteArray(iList.get(i), 2);
            d[4 + i * 2] = fb[1];
            d[4 + i * 2 + 1] = fb[0];
        }
        return d;
    }

    /**
     * toByteArray
     *
     * @param iSource   iSource
     * @param iArrayLen iArrayLen
     * @return toByteArray
     */
    public static byte[] toByteArray(int iSource, int iArrayLen) {
        byte[] bLocalArr = new byte[iArrayLen];
        for (int i = 0; (i < FOUR_NUMBER) && (i < iArrayLen); i++) {
            bLocalArr[i] = (byte) (iSource >> 8 * i & FACTOR_FF);
        }
        return bLocalArr;
    }
}
