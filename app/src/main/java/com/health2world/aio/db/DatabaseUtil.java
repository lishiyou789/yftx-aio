package com.health2world.aio.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.misc.JavaxPersistence;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.util.Arrays;

/**
 * Created by lishiyou on 2018/10/23 0023.
 */

public class DatabaseUtil {
    public static final String TAG = "DatabaseUtil";

    /**
     * 数据库表操作类型
     */
    public enum OPERATION_TYPE {
        /**
         * 表新增字段
         */
        ADD,
        /**
         * 表删除字段
         */
        DELETE
    }

    /**
     * 升级表，增加字段
     *
     * @param db
     * @param clazz
     */
    public static <T> void upgradeTable(SQLiteDatabase db, ConnectionSource cs, Class<T> clazz, OPERATION_TYPE type) {
        String tableName = extractTableName(clazz);
        db.beginTransaction();
        try {
            //Rename table
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            db.execSQL(sql);

            //Create table
            try {
                sql = TableUtils.getCreateTableStatements(cs, clazz).get(0);
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
                TableUtils.createTable(cs, clazz);
            }

            //Load data
            String columns;
            if (type == OPERATION_TYPE.ADD) {
                columns = Arrays.toString(getColumnNames(db, tempTableName)).replace("[", "").replace("]", "");
            } else if (type == OPERATION_TYPE.DELETE) {
                columns = Arrays.toString(getColumnNames(db, tableName)).replace("[", "").replace("]", "");
            } else {
                throw new IllegalArgumentException("OPERATION_TYPE error");
            }
            sql = "INSERT INTO " + tableName +
                    " (" + columns + ") " +
                    " SELECT " + columns + " FROM " + tempTableName;
            db.execSQL(sql);

            //Drop temp table
            sql = "DROP TABLE IF EXISTS " + tempTableName;
            db.execSQL(sql);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    /**
     * 获取表名(ormlite DatabaseTableConfig.java)
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        } else {
            /*
             * NOTE: to remove javax.persistence usage, comment the following line out
             */
            name = JavaxPersistence.getEntityName(clazz);
            if (name == null) {
                // if the name isn't specified, it is the class name lowercased
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }

    /**
     * 获取表的列名
     *
     * @param db
     * @param tableName
     * @return
     */
    private static String[] getColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex("name");
                if (columnIndex == -1) {
                    return null;
                }
                int index = 0;
                columnNames = new String[cursor.getCount()];
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    columnNames[index] = cursor.getString(columnIndex);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columnNames;
    }
}
