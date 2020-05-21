package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class WithdrawalRecordBean implements MultiItemEntity {

    /**
     * id : 10001
     * uname : 123
     * bankcard : 123
     * cash_asset : 0.01
     * status : 已提交
     * time : 2018-12-03
     */

    private int id;
    private String uname;
    private String bankcard;
    private String cash_asset;
    private String status;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getCash_asset() {
        return cash_asset;
    }

    public void setCash_asset(String cash_asset) {
        this.cash_asset = cash_asset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int getItemType() {
        return -0xff;
    }
}
