package com.konsung.listen;

/**
 * Created by YYX on 2017/7/26 0026.
 * 测量项的枚举类
 */

public enum Measure {
    NIBP("血压", "90-139/60-89mmHg"), //血压
    SPO2("血氧", "94-100%"), //血氧
    PR("脉率/心率", "60-100bpm"), //脉率
    ECG("心电", "------"), //心电
    TEMP("体温", "34.7-37.8℃"), //体温
    GLU("血糖", "3.9-6.1mmol/L"), //血糖
    URINE("尿常规", "------"), //尿常规
    CHOL("总胆固醇", "3.12-5.18mmol/L"), //总胆固醇
    UA("尿酸", "202-416μmmol/L"), //尿酸
    BLOOD("血脂四项", "------"), //血脂
    HB("血红蛋白", "120-160g/L / 40-50%"),//血红蛋白/积压值
    HCT("红细胞比容", "37-45%"),//血红蛋白/积压值
    FHR("胎心监", "------"),//胎心监
    HEIGHT("身高", "------"),//身高
    WEIGHT("体重", "------"),//体重
    CRP("C反应蛋白", "<10mg/L"),//C反应蛋白
    Hs_CRP("超敏C反应蛋白", "0-3mg/L"),
    SAA("血清淀粉样蛋白A", "<10mg/L"),//
    PCT("降钙素原", "0-0.05ng/mL"),
    MYO("心肌三项", "------"),
    WBC("白细胞", "4.0-10.0*10^9/L"),
    GHB("糖化血红蛋白", "------"),
    DS100A("血脂检测", "------");//大树血脂

    private String name = "";
    private String normalValue = "";

    Measure(String name, String normalValue) {
        this.name = name;
        this.normalValue = normalValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }
}
