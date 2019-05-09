package com.health2world.aio.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/15 0015.
 */

public class UpgradeInfo implements Serializable {
    //是否需要升级（1：是 2：否）
    private int isUpgrade;
    //是否强制升级（1：是 2：否）
    private int enforceFlag;
    //当前版本
    private int currentVersion = 0;
    //最新版本号名称
    private String versionName = "";
    //最新版本下载地址
    private String url;
    //版本更新记录
    private String updateContent;

    public int getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(int isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public int getEnforceFlag() {
        return enforceFlag;
    }

    public void setEnforceFlag(int enforceFlag) {
        this.enforceFlag = enforceFlag;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }
}
