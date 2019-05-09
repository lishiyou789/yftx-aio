package com.health2world.aio.config;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Xml;

import com.health2world.aio.MyApplication;
import com.health2world.aio.R;
import com.health2world.aio.bean.MeasureItem;
import com.health2world.aio.bean.MeasureState;
import com.health2world.aio.util.Logger;
import com.konsung.listen.Measure;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class MeasureConfig {
    //编号代表排序的优先位置 位置可以调动 但是不能空位

    public static final int NIBP = 0;//血压
    public static final int SPO2 = 1;//血氧
    public static final int PR = 2;//脉率
    public static final int ECG = 3;//心电
    public static final int TEMP = 4;//体温
    public static final int URINE = 5;//尿常规
    public static final int DS100A = 6;//大树血脂 血脂八项
    public static final int GLU = 7;//血糖
    public static final int UA = 8;//尿酸
    public static final int CHOL = 9;//总胆固醇
    public static final int BLOOD = 10;//血脂四项
    public static final int HB = 11;//血红蛋白
    public static final int GHB = 12;//糖化血红蛋白
    public static final int HEIGHT = 13;//身高
    public static final int WEIGHT = 14;//体重
    public static final int WBC = 15;//白细胞
    public static final int CRP = 16;//C反应蛋白
    public static final int HSCRP = 17;//超敏CRP
    public static final int SAA = 18;//血清淀粉样蛋白A
    public static final int PCT = 19;//降钙素元
    public static final int MYO = 20;//心肌三项


    public enum Device {
        BENECHECK("BeneCheck", 172015775),//54575263
        EA12("EA12", 169918623),//52478111
        OGM111("OGM111", 167821727);//50381215
        // 成员变量
        private String device;
        private int config;

        // 构造方法
        Device(String device, int config) {
            this.device = device;
            this.config = config;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public int getConfig() {
            return config;
        }

        public void setConfig(int config) {
            this.config = config;
        }
    }


    public static List<MeasureItem> getMeasureList(Context context) {
        List<MeasureItem> dataList = new ArrayList<>();
        //获取用户配置的测量项进行加载
        String items = MyApplication.getInstance().getMeasureConfig();
        Logger.e("zrl", "测量项： " + items);
        for (int i = 0; i < items.length(); i++) {
            boolean isOpen = Integer.valueOf(String.valueOf(items.charAt(i))) == 1;
            if (i == MeasureConfig.NIBP && isOpen)
                dataList.add(new MeasureItem(Measure.NIBP, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.SPO2 && isOpen)
                dataList.add(new MeasureItem(Measure.SPO2, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.PR && isOpen)
                dataList.add(new MeasureItem(Measure.PR, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.ECG && isOpen)
                dataList.add(new MeasureItem(Measure.ECG, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.TEMP && isOpen)
                dataList.add(new MeasureItem(Measure.TEMP, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.URINE && isOpen)
                dataList.add(new MeasureItem(Measure.URINE, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.GLU && isOpen)
                dataList.add(new MeasureItem(Measure.GLU, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.CHOL && isOpen)
                dataList.add(new MeasureItem(Measure.CHOL, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.UA && isOpen)
                dataList.add(new MeasureItem(Measure.UA, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.BLOOD && isOpen)
                dataList.add(new MeasureItem(Measure.BLOOD, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.HB && isOpen)
                dataList.add(new MeasureItem(Measure.HB, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.HEIGHT && isOpen)
                dataList.add(new MeasureItem(Measure.HEIGHT, MeasureState.MEASURE_READY, context.getString(R.string.click_to_input)));
            if (i == MeasureConfig.WEIGHT && isOpen)
                dataList.add(new MeasureItem(Measure.WEIGHT, MeasureState.MEASURE_READY, context.getString(R.string.click_to_input)));
            if (i == MeasureConfig.DS100A && isOpen)
                dataList.add(new MeasureItem(Measure.DS100A, MeasureState.MEASURE_NONE, MyApplication.getInstance().isFatConnect()));
            if (i == MeasureConfig.WBC && isOpen)
                dataList.add(new MeasureItem(Measure.WBC, MeasureState.MEASURE_READY, MyApplication.getInstance().isWbcConnect()));
            if (i == MeasureConfig.CRP && isOpen)
                dataList.add(new MeasureItem(Measure.CRP, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.SAA && isOpen)
                dataList.add(new MeasureItem(Measure.SAA, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.PCT && isOpen)
                dataList.add(new MeasureItem(Measure.PCT, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.MYO && isOpen)
                dataList.add(new MeasureItem(Measure.MYO, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.GHB && isOpen)
                dataList.add(new MeasureItem(Measure.GHB, MeasureState.MEASURE_READY));
            if (i == MeasureConfig.HSCRP && isOpen)
                dataList.add(new MeasureItem(Measure.Hs_CRP, MeasureState.MEASURE_READY));

        }
        return dataList;
    }

    //DeviceManager 写入配置文件
    public static void writeXmlConfig() {
        try {
            File fileP;
            if (TextUtils.equals(android.os.Build.VERSION.RELEASE, AppConfig.SYSTEM_VERSION_5_1))
                // 指定流目录
                fileP = new File("/data/local");
            else
                fileP = new File(Environment.getExternalStorageDirectory().toString() + "/Konsung/DeviceManager");
            if (!fileP.exists())
                fileP.mkdirs();
            File file = new File(fileP, "DeviceConfig.xml");
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
                accessPermissions(file.getPath());
            }

            XmlSerializer serializer = Xml.newSerializer();
            FileOutputStream os = new FileOutputStream(file);
            // 设置指定目录
            serializer.setOutput(os, "UTF-8");
            // 设置文件头
            serializer.startDocument("UTF-8", true);
            String enter = System.getProperty("line.separator");
            //换行
            changeLine(serializer, enter);
            serializer.startTag(null, "DeviceConfig");
            changeLine(serializer, enter);
            serializer.startTag(null, "DeviceList");
            changeLine(serializer, enter);
            //连接方式 0 USB连接；1串口连接；2WIFI连接；3蓝牙连接

            //极光打印机
            serializer.startTag(null, "P2500");
            changeLine(serializer, enter);
            serializer.startTag(null, "DeviceConnect");
            serializer.text("0");
            serializer.endTag(null, "DeviceConnect");
            changeLine(serializer, enter);
            serializer.endTag(null, "P2500");
            changeLine(serializer, enter);

            //量点CRP
            serializer.startTag(null, "NepQDV1");
            changeLine(serializer, enter);
            serializer.startTag(null, "DeviceConnect");
            serializer.text("0");
            serializer.endTag(null, "DeviceConnect");
            changeLine(serializer, enter);
            serializer.endTag(null, "NepQDV1");

            changeLine(serializer, enter);
            serializer.endTag(null, "DeviceList");

            changeLine(serializer, enter);
            //结束头部
            serializer.endTag(null, "DeviceConfig");
            serializer.endDocument();
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改文件权限 666 为可读可写
     *
     * @param path 文件路径
     */
    public static void accessPermissions(String path) {
        Process p;
        int status = -1;
        try {
            p = Runtime.getRuntime().exec("chmod 666 " + path);
            status = p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void changeLine(XmlSerializer serializer, String enter) {
        try {
            serializer.text(enter);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
