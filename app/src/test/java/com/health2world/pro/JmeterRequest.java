package com.health2world.pro;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aio.health2world.utils.DateUtil;

/**
 * Created by lishiyou on 2018/11/15 0015.
 */

public class JmeterRequest {

    String APPSECRET_VALUES = "44d6d569341947ec947c711a18574de5";

    @Test
    public void sign() {
        String time = DateUtil.getDate1(new Date(1555565970000L));
        System.out.println(time);
    }

    private String getSign(String paramsJson) {

        HashMap<String, Object> rootMap = new Gson().fromJson(paramsJson, HashMap.class);

        List<Map.Entry<String, Object>> mapList = new ArrayList<>(rootMap.entrySet());

        Collections.sort(mapList, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> mapping1,
                               Map.Entry<String, Object> mapping2) {
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> mapping : mapList) {
            sb.append(mapping.getKey()).append("=").append(mapping.getValue());
        }
        String sign = sb.append(APPSECRET_VALUES).toString();
        return getMD5String(sign);

    }

    private String getMD5String(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }

        byte[] byteArray = messageDigest.digest();

        // StringBuffer md5StrBuff = new StringBuffer();
        StringBuilder md5StrBuff = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

//		return md5StrBuff.substring(8, 24).toString().toUpperCase();
        return md5StrBuff.toString().toLowerCase();
    }


}
