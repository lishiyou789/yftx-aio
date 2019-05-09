package com.konsung.listen;

/**
 * 刷身份证身份证监听
 */

public interface IdCardListen {
    /**
     * 身份证监听
     */
    /**
     * @param name     姓名
     * @param idCard   身份证号码
     * @param sex      性别
     * @param type     类型
     * @param birthday 生日
     * @param picture  照片
     * @param address  地址
     */
    void onListen(String name, String idCard, int sex, int type,
                  String birthday, String picture, String address);

}
