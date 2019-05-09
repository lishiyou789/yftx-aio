package com.health2world.aio.provider;

/**
 * 公卫字典对照表
 * Created by lishiyou on 2018/4/19 0019.
 */

public class PublicHealthDictionary {

    /**
     * 匹配性别
     *
     * @param sexDictionary
     * @return
     */
    public static int matchSexy(String sexDictionary) {
        if (sexDictionary.equals("1V1.0GENDER"))//男
            return 1;
        if (sexDictionary.equals("2V1.0GENDER"))//女
            return 0;
        if (sexDictionary.equals("9V1.0GENDER"))//未知
            return 2;
        if (sexDictionary.equals("2"))//女
            return 0;
        if (sexDictionary.equals("1"))//男
            return 1;
        return 2;//未知
    }

    //匹配户籍类型
    public static String matchResidenceType(String residence_type_dictionary) {
        if ("1V1.0RESIDENCE_TYPE".equals(residence_type_dictionary))
            return "户籍";
        if ("2V1.0RESIDENCE_TYPE".equals(residence_type_dictionary))
            return "非户籍";
        return "";
    }

    //匹配药物过敏史
    public static String matchAllergic(String allergicDictionary) {
        if ("2V1.0ALLERGIC_HISTORY_CODE".equals(allergicDictionary))
            return "青霉素";
        if ("3V1.0ALLERGIC_HISTORY_CODE".equals(allergicDictionary))
            return "磺胺";
        if ("5V1.0ALLERGIC_HISTORY_CODE".equals(allergicDictionary))
            return "其他";
        if ("1V1.0ALLERGIC_HISTORY_CODE".equals(allergicDictionary))
            return "无";
        if ("4V1.0ALLERGIC_HISTORY_CODE".equals(allergicDictionary))
            return "链霉素";
        return "";
    }

    //匹配血型
    public static String matchBloodType(String bloodDictionary) {
        if ("1V1.0BLOOD_TYPE".equals(bloodDictionary))
            return "A型";
        if ("2V1.0BLOOD_TYPE".equals(bloodDictionary))
            return "B型";
        if ("3V1.0BLOOD_TYPE".equals(bloodDictionary))
            return "AB型";
        if ("4V1.0BLOOD_TYPE".equals(bloodDictionary))
            return "O型";
        if ("5V1.0BLOOD_TYPE".equals(bloodDictionary))
            return "不详";
        return "";
    }

    //匹配婚姻状况
    public static String matchMaritalStatus(String maritalDictionary) {
        if ("1V1.0MARITAL_STATUS".equals(maritalDictionary))
            return "未婚";
        if ("2V1.0MARITAL_STATUS".equals(maritalDictionary))
            return "已婚";
        if ("3V1.0MARITAL_STATUS".equals(maritalDictionary))
            return "丧偶";
        if ("4V1.0MARITAL_STATUS".equals(maritalDictionary))
            return "离婚";
        if ("9V1.0MARITAL_STATUS".equals(maritalDictionary))
            return "未说明的婚姻状况";
        return "";
    }

    //匹配学历
    public static String matchEducation(String educationDictionary) {
        if ("1V1.0EDUCATION".equals(educationDictionary))
            return "文盲或半文盲";
        if ("2V1.0EDUCATION".equals(educationDictionary))
            return "小学";
        if ("3V1.0EDUCATION".equals(educationDictionary))
            return "初中";
        if ("6V1.0EDUCATION".equals(educationDictionary))
            return "大学本科";
        if ("11V1.0EDUCATION".equals(educationDictionary))
            return "研究生";
        if ("12V1.0EDUCATION".equals(educationDictionary))
            return "大学本科和专科学校";
        if ("13V1.0EDUCATION".equals(educationDictionary))
            return "中等专业学校";
        if ("14V1.0EDUCATION".equals(educationDictionary))
            return "技工学校";
        if ("15V1.0EDUCATION".equals(educationDictionary))
            return "高中";
        if ("99V1.0EDUCATION".equals(educationDictionary))
            return "不详";
        return "";
    }
}
