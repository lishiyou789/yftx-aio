package com.health2world.aio.db;

import android.database.sqlite.SQLiteDatabase;

import com.health2world.aio.MyApplication;
import com.health2world.aio.bean.DoctorBean;
import com.health2world.aio.bean.HistoryAccount;
import com.health2world.aio.bean.TagInfo;
import com.health2world.aio.util.Logger;
import com.j256.ormlite.dao.Dao;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.ResidentBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 数据操作类
 * Created by Administrator on 2018/7/4 0004.
 */

public class DBManager {

    static final String TAG = "DBManager";

    private static final DBManager ourInstance = new DBManager();

    private final DBOpenHelper dbHelper;
    //居民
    private Dao<ResidentBean, Integer> residentDao;
    //测量数据
    private Dao<MeasureBean, Integer> measureDao;
    //登陆者信息
    private Dao<DoctorBean, Integer> doctorDao;
    //历史搜索记录
    private Dao<HistoryAccount, Integer> historyDao;
    //标签信息
    private Dao<TagInfo, Integer> tagInfoDao;

    public static DBManager getInstance() {
        return ourInstance;
    }

    private DBManager() {
        dbHelper = new DBOpenHelper(MyApplication.getInstance());
    }


    public Dao<DoctorBean, Integer> getDoctorDao() {
        if (doctorDao == null) {
            try {
                doctorDao = dbHelper.getDao(DoctorBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return doctorDao;
    }

    public Dao<MeasureBean, Integer> getMeasureDao() {
        if (measureDao == null) {
            try {
                measureDao = dbHelper.getDao(MeasureBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return measureDao;
    }

    public Dao<ResidentBean, Integer> getResidentDao() {
        if (residentDao == null) {
            try {
                residentDao = dbHelper.getDao(ResidentBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return residentDao;
    }

    public Dao<HistoryAccount, Integer> getHistoryDao() {
        if (historyDao == null) {
            try {
                historyDao = dbHelper.getDao(HistoryAccount.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return historyDao;
    }

    public Dao<TagInfo, Integer> getTagInfoDao() {
        if (tagInfoDao == null) {
            try {
                tagInfoDao = dbHelper.getDao(TagInfo.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tagInfoDao;
    }

    public void closeDB() {
        dbHelper.close();
    }

    public void deleteTable() {
        //truncate(drop) table if exists
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from t_resident");
        db.execSQL("delete from t_doctor");
    }

    /**
     * 获取当前登录者信息
     */
    public DoctorBean getCurrentDoctor() {
        DoctorBean doctor = null;
        int doctorId = MyApplication.getInstance().getDoctorId();
        try {
            doctor = getDoctorDao().queryForId(doctorId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    /***
     * 获取标签信息
     * @return tagInfoList 标签信息集合
     */
    public List<TagInfo> getTagInfoList() {
        List<TagInfo> tagInfoList = new ArrayList<>();
        try {
            List<TagInfo> list = getTagInfoDao().queryForAll();
            if (list != null)
                tagInfoList.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tagInfoList;
    }


    /**
     * 导出数据库到一体机的外部存储
     */
    public void exportDb() {
        //判断SD卡是否存在
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals
                (android.os.Environment.getExternalStorageState());
        if (!sdExist) {
            Logger.e(TAG, "SD卡不存在，请加载SD卡！");
            return;
        }
        Observable.just(DBOpenHelper.DATABASE_NAME)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String dbName) {
                        File dbFile = MyApplication.getInstance().getDatabasePath(dbName);
                        if (!dbFile.exists()) {
                            throw new RuntimeException("database file not exists");
                        } else
                            return dbFile;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "export db error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(File file) {
                        //获取sd卡路径
                        String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                        //数据库所在目录
                        dbDir += "/health2world";
                        String dbPath = dbDir + "/" + DBOpenHelper.DATABASE_NAME;//数据库路径
                        //判断目录是否存在，不存在则创建该目录
                        File dirFile = new File(dbDir);
                        if (!dirFile.exists())
                            dirFile.mkdirs();
                        InputStream fis = null;
                        OutputStream fos = null;
                        try {
                            fis = new FileInputStream(file);
                            fos = new FileOutputStream(new File(dbPath));
                            byte[] buf = new byte[1024];
                            int length;
                            while ((length = fis.read(buf)) != -1) {
                                fos.write(buf, 0, length);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fis != null)
                                    fis.close();
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Logger.e(TAG, "export db success: " + dbPath);
                    }
                });
    }
}
