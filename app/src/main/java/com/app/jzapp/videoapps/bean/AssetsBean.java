package com.app.jzapp.videoapps.bean;

public class AssetsBean {

    /**
     * uid : 10003
     * asset : 1.00
     * residual_asset : 0.99
     * first_level : 0
     * second_level : 0
     * third_level : 0
     * fourth_level : 0
     * brokerage : 5
     */

    private int uid;
    private String asset;
    private String residual_asset;
    private String first_level;
    private String second_level;
    private String third_level;
    private String fourth_level;
    private String brokerage;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getResidual_asset() {
        return residual_asset;
    }

    public void setResidual_asset(String residual_asset) {
        this.residual_asset = residual_asset;
    }

    public String getFirst_level() {
        return first_level;
    }

    public void setFirst_level(String first_level) {
        this.first_level = first_level;
    }

    public String getSecond_level() {
        return second_level;
    }

    public void setSecond_level(String second_level) {
        this.second_level = second_level;
    }

    public String getThird_level() {
        return third_level;
    }

    public void setThird_level(String third_level) {
        this.third_level = third_level;
    }

    public String getFourth_level() {
        return fourth_level;
    }

    public void setFourth_level(String fourth_level) {
        this.fourth_level = fourth_level;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }
}
