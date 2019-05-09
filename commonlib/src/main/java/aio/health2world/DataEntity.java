package aio.health2world;

import java.io.Serializable;

/**
 * 基本数据模型
 * Created by lishiyou on 2017/1/19.
 */

public class DataEntity implements Serializable, Cloneable {

    private int id;

    private String tag;

    private String text;

    private String extra = "";

    private boolean isChecked;

    public DataEntity() {
    }

    public DataEntity(String tag) {
        this.tag = tag;
    }

    public DataEntity(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public DataEntity(int id, String tag, String text) {
        this.id = id;
        this.tag = tag;
        this.text = text;
    }

    public DataEntity(int id, String tag, String text, String extra) {
        this.id = id;
        this.tag = tag;
        this.text = text;
        this.extra = extra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public DataEntity clone() {
        DataEntity entity = null;
        try {
            entity = (DataEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", text='" + text + '\'' +
                ", extra='" + extra + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
