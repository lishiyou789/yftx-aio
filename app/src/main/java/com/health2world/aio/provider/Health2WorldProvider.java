package com.health2world.aio.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.health2world.aio.MyApplication;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.db.DBManager;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lishiyou on 2018/8/3 0003.
 */

public class Health2WorldProvider extends ExtraProvider {

    /**
     * 档案下载之后插入居民
     *
     * @param uri    uri
     * @param values 数据
     * @return
     */
    @Override
    Uri insertCitizen(Uri uri, ContentValues values) { // 姓名

        String name = values.containsKey("name") ? values.getAsString("name") : "";
        // 身份证,为了查询方便，如果没有身份证，公卫会传-1过来
        String idcard = values.containsKey("idcard") ? values.getAsString("idcard") : "";
        // 年龄，如果没有设置年龄，公卫会传-1过来
        String age = values.containsKey("age") ? values.getAsString("age") : "0";
        // 性别
        String gender = values.containsKey("gender") ? values.getAsString("gender") : "2";
        // 体重
        String weight = values.containsKey("weight") ? values.getAsString("weight") : "0";
        // 身高
        String height = values.containsKey("height") ? values.getAsString("height") : "0";

        ResidentBean resident = new ResidentBean();
        resident.setName(name);
        resident.setIdentityCard(idcard);
        resident.setAge(Integer.valueOf(age));
        resident.setSexy(Integer.valueOf(gender));
        resident.setHeight(height);
        resident.setWeight(weight);
        try {
            DBManager.getInstance().getResidentDao().createOrUpdate(resident);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 查询当前居民
     *
     * @param cursor 空数据
     * @return
     */
    @Override
    Cursor queryCurrentCitizen(MatrixCursor cursor) {
        String currentPatient;
        List<ResidentBean> list = null;
        try {
            list = DBManager.getInstance().getResidentDao().queryForEq("identityCard",
                    MyApplication.getInstance().getCurrentIdentityCard());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list == null || list.size() == 0) {
            currentPatient = "";
        } else {
            currentPatient = new Gson().toJson(list.get(0));
        }
        cursor.addRow(new Object[]{currentPatient});
        return cursor;
    }

    //查询当前居民的测量数据
    @Override
    Cursor queryCurrentMeasureData(MatrixCursor cursor) {
        String idCard = MyApplication.getInstance().getCurrentIdentityCard();
        List<MeasureBean> query1 = null;
        if (!TextUtils.isEmpty(idCard)) {
            try {
                query1 = DBManager.getInstance().getMeasureDao().queryBuilder()
                        .orderBy("checkDate", false)
                        .limit(1)
                        .where()
                        .eq("idCard", idCard)
                        .query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String data1;
        if (query1 != null && query1.size() > 0) {
            data1 = new Gson().toJson(query1.get(0));
        } else {
            data1 = "";
        }
        cursor.addRow(new Object[]{data1});
        return cursor;
    }

    //查询当前用户（医生）
    @Override
    Cursor queryCurrentUser(MatrixCursor cursor) {
        String doctorId = MyApplication.getInstance().getDoctorId() + "";
        String currentDoctor = "";
        List<DoctorBean> loginInfoList = null;
        try {
            loginInfoList = DBManager.getInstance().getDoctorDao().queryForEq("doctorId", doctorId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (loginInfoList != null && loginInfoList.size() > 0) {
            currentDoctor = new Gson().toJson(loginInfoList.get(0));
        }
        cursor.addRow(new Object[]{currentDoctor});
        return cursor;
    }

    //公卫后台的服务器地址
    @Override
    Cursor queryIpAddress(MatrixCursor cursor) {
        cursor.addRow(new Object[]{MyApplication.getInstance().getPublicHealthUrl()});
        return cursor;
    }

    @Override
    String getVersion() {
        return AppVersion.VERSION_1;
    }

    @Override
    String getSkinStyle() {
        return SkinStyle.SKIN_SKY_BLUE;
    }

    @Override
    public String getType(Uri uri) {
        return AppType.TYPE_COALITION;
    }
}
