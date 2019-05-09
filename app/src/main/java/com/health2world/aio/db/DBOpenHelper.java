package com.health2world.aio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.health2world.aio.BuildConfig;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.HistoryAccount;
import com.health2world.aio.bean.TagInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.sql.SQLException;

/**
 * Created by lishiyou on 2017/6/29.
 */

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {

    public static final String TAG = "DBOpenHelper";
    /*** 数据库名称**/
    public static String DATABASE_NAME = "health2world_aio.db";
    /*** 数据库版本**/
    public static int DATABASE_VERSION = 231;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, DoctorBean.class);
            TableUtils.createTableIfNotExists(connectionSource, ResidentBean.class);
            TableUtils.createTableIfNotExists(connectionSource, MeasureBean.class);
            TableUtils.createTableIfNotExists(connectionSource, HistoryAccount.class);
            TableUtils.createTableIfNotExists(connectionSource, TagInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int newVersion, int oldVersion) {
        try {
            TableUtils.dropTable(connectionSource, DoctorBean.class, true);
            TableUtils.dropTable(connectionSource, ResidentBean.class, true);
            TableUtils.dropTable(connectionSource, MeasureBean.class, true);
            TableUtils.dropTable(connectionSource, TagInfo.class, true);
            if (newVersion - oldVersion < 2) {
                DatabaseUtil.upgradeTable(db, connectionSource, HistoryAccount.class, DatabaseUtil.OPERATION_TYPE.ADD);
            } else {
                TableUtils.dropTable(connectionSource, HistoryAccount.class, true);
            }
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
    }

}
