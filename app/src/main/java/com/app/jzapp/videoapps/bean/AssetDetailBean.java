package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class AssetDetailBean implements MultiItemEntity {

    @Override
    public int getItemType() {
        return -0xff;
    }

    /**
     * money : -0.01
     * time : 2018-12-05
     */

    private String money;
    private String time;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
