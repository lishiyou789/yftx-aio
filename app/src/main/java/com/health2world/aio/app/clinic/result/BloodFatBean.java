package com.health2world.aio.app.clinic.result;

import java.io.Serializable;

/**
 * Created by lishiyou on 2018/8/22 0022.
 */

public class BloodFatBean implements Serializable {
    public String name = ""; //参数名称
//    public int type; //血脂子项类型
    public float min = 0f; //参考范围下限
    public float max = 0f; //参考范围上限
    public float viewMin = 0f; //显示范围下限
    public float viewMax = 0f; //显示范围上限
    public String value = ""; //测量结果
    public boolean normal = true; //是否异常 默认正常

}
