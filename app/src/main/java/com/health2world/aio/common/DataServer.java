package com.health2world.aio.common;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.health2world.aio.MyApplication;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.config.AppConfig;
import com.health2world.aio.db.DBManager;
import com.health2world.aio.http.ApiRequest;
import com.health2world.aio.http.HttpSubscriber;
import com.health2world.aio.util.Logger;
import com.j256.ormlite.support.DatabaseConnection;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import aio.health2world.http.HttpResult;
import aio.health2world.utils.NetworkUtil;
import aio.health2world.utils.SPUtils;

/**
 * Created by lishiyou on 2017/7/24 0024.
 */

public class DataServer {

    //任务状态( 0：待测量；1：待开方；2：待解读，3：已开方)
    //待办类型
    public static String getTaskType(int type) {
        switch (type) {
            case 0:
                return "待测量";
            case 1:
                return "待开方";
            case 2:
                return "待解读";
            case 3:
                return "已开方";
            case 5:
                return "监测中";
            case 7:
                return "待建档";
        }
        return "未知状态";
    }


    //0女 1男 2未知
    public static String getSexNick(int type) {
        switch (type) {
            case 0:
                return "女";
            case 1:
                return "男";
            case 2:
                return "--";
        }
        return "--";
    }

    //默认EA-12设备的配置
    public static String getDeviceName() {
        int gluDevice = (int) SPUtils.get(MyApplication.getInstance(), AppConfig.DEVICE_GLU, 1);
        switch (gluDevice) {
            case 0:
                return "BeneCheck";
            case 1:
                return "EA-12";
            case 2:
                return "OGM-111";
        }
        return "";
    }

    /**
     * 根据标签Id 获取标签的简称
     * 输入String 返回String 逗号隔开
     */

    public static String getTagShortName(String tagIds) {
        String shortName = "";
        if (TextUtils.isEmpty(tagIds))
            return shortName;
        List<TagInfo> tagInfoList = DBManager.getInstance().getTagInfoList();
        for (TagInfo info : tagInfoList) {
            for (String s : tagIds.split(",")) {
                if (Integer.valueOf(s) == info.getTagId()) {
                    shortName += info.getShortName() + ",";
                    break;
                }
            }
        }
        if (!TextUtils.isEmpty(shortName))
            shortName = shortName.substring(0, shortName.length() - 1);
        return shortName;
    }

    public static void initData() {
        //从服务器获取标签信息
        ApiRequest.getTagList(new HttpSubscriber() {
            @Override
            public void onNext(HttpResult result) {
                if (result.code.equals(AppConfig.SUCCESS)) {
                    try {
                        JSONArray array = new JSONArray(new Gson().toJson(result.data));
                        DatabaseConnection connection = DBManager.getInstance().getResidentDao().startThreadConnection();
                        Savepoint savepoint = connection.setSavePoint(null);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.optJSONObject(i);
                            TagInfo info = TagInfo.parseBean(object);
                            DBManager.getInstance().getTagInfoDao().createOrUpdate(info);
                        }
                        connection.commit(savepoint);
                        DBManager.getInstance().getTagInfoDao().endThreadConnection(connection);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 上传失败的数据将在这里再次进行上传
     */
    public static synchronized void uploadMeasureData() {
        if (!NetworkUtil.isConnected(MyApplication.getInstance()))
            return;
        List<MeasureBean> data = null;

        try {
            data = DBManager.getInstance().getMeasureDao().queryBuilder().where().eq("upload", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data != null && data.size() > 0) {
            for (final MeasureBean bean : data) {
                List<ResidentBean> list = null;
                int sex = 1;
                try {
                    list = DBManager.getInstance().getResidentDao().queryBuilder().where().eq("patientId", bean.getPatientId()).query();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (list != null && list.size() > 0) {
                    sex = list.get(0).getSexy();
                }
                final ReentrantLock lock = new ReentrantLock();
                lock.lock();
                ApiRequest.uploadMedicalData(bean.getDataId(), bean.getPatientId(), sex, bean, new HttpSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        try {
                            bean.setUpload(false);
                            DBManager.getInstance().getMeasureDao().update(bean);
                        } catch (SQLException pE) {
                            pE.printStackTrace();
                        }
                        lock.unlock();
                    }

                    @Override
                    public void onNext(HttpResult result) {
                        //上传成功则标记该数据为success
                        if (result.code.equals(AppConfig.SUCCESS)) {
                            bean.setUpload(true);
                            try {
                                DBManager.getInstance().getMeasureDao().update(bean);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        lock.unlock();
                    }
                });
            }
        }
    }
}
