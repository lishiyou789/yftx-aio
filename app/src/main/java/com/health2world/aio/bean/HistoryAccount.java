package com.health2world.aio.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/26 0026.
 */
@DatabaseTable(tableName = "t_history")
public class HistoryAccount implements Serializable {

    @DatabaseField(id = true)
    private String data;
    @DatabaseField
    private long time;

    public HistoryAccount() {
    }

    public HistoryAccount(String data, long time) {
        this.data = data;
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
