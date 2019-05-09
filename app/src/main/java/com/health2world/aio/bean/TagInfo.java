package com.health2world.aio.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 标签数据模型
 * Created by lishiyou on 2018/9/14 0014.
 */
@DatabaseTable(tableName = "t_tag_info")
public class TagInfo implements Serializable {
    //       "tagId":1,
//               "name":"高血压患者",
//               "shortName":"压",
//               "type":1,
//               "serviceIds":""
    @DatabaseField(id = true, dataType = DataType.INTEGER)
    private int tagId;
    @DatabaseField(dataType = DataType.INTEGER)
    private int type;//1系统定义 2诊所定义 3人群 4病种
    @DatabaseField(dataType = DataType.STRING)
    private String name = "";
    @DatabaseField(dataType = DataType.STRING)
    private String shortName = "";
    @DatabaseField(dataType = DataType.STRING)
    private String serviceIds = "";
    private boolean checked = false;

    public TagInfo() {
    }

    public TagInfo(int tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public static TagInfo parseBean(JSONObject object) {
        TagInfo info = new TagInfo();
        int tagId = object.optInt("tagId");
        int type = object.optInt("type");
        String name = object.optString("name");
        String shortName = object.optString("shortName");
        String serviceIds = object.optString("serviceIds");
        info.setTagId(tagId);
        info.setType(type);
        info.setName(name);
        info.setShortName(shortName);
        info.setServiceIds(serviceIds);
        return info;
    }
}
