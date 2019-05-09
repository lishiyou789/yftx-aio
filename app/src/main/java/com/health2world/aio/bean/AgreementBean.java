package com.health2world.aio.bean;


import java.io.Serializable;

/**
 * @author Runnlin
 * @date 2018/7/31/0031.
 */

public class AgreementBean implements Serializable {
    //协议ID
    private int protocolId;
    //协议类型（0：主协议 1：补充协议）
    private int type;
    //协议类型 0签约 1 解约
    private String delFlag = "";
    //协议名称
    private String name;
    //签约图片（多个逗号分隔）
    private String signImgUrl;
    //图标
    private String autograph;
    //有效期开始日期
    private long startTime;
    //有效期结束日期
    private long endTime;
    //协议内容（HTML格式）
    private String content;
    //选中状态
    private boolean check;

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(int protocoId) {
        this.protocolId = protocoId;
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

    public String getSignImgUrl() {
        return signImgUrl;
    }

    public void setSignImgUrl(String signImgUrl) {
        this.signImgUrl = signImgUrl;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}

