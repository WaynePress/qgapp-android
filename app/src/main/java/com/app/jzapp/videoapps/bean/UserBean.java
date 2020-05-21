package com.app.jzapp.videoapps.bean;

public class UserBean {

    @Override
    public String toString() {
        return "UserBean{" +
                "uid=" + uid +
                ", pic='" + pic + '\'' +
                ", randomnum='" + randomnum + '\'' +
                ", mobile='" + mobile + '\'' +
                ", vipendtime='" + vipendtime + '\'' +
                ", is_vip=" + is_vip +
                ", daycount=" + daycount +
                ", vipotype=" + vipotype +
                ", invitecode='" + invitecode + '\'' +
                ", safecode='" + safecode + '\'' +
                ", downcount=" + downcount +
                ", lookcount=" + lookcount +
                ", lookedcount=" + lookedcount +
                ", residual_asset='" + residual_asset + '\'' +
                ", is_safe=" + is_safe +
                ", sharecount=" + sharecount +
                '}';
    }

    /**
     * uid : 10001
     * pic :
     * randomnum : 1540367246510fhv
     * mobile :
     * email :
     * vipendtime : 1970-01-01 00:00:00
     * is_vip : 0
     */

    private int uid;
    private String pic;
    private String randomnum;
    private String mobile;
//    private String email;
    private String vipendtime;
    //是否是vip 1：是 0：不是
    private int is_vip;
    private int daycount;
    /**
     * vipotype : 0
     * invitecode : L54M
     * safecode : 3a0f7c8450ea3fb34b5442e2ffbc3709
     * downcount : 0
     * lookcount : 0
     * lookedcount : 0
     * residual_asset : 0.00
     * is_safe : 1
     * sharecount : 0
     */

    //0：无 1：月卡 5：季卡 10：年卡
    private String vipotype;
    private String invitecode;
    private String safecode;
    private int downcount;
    private int lookcount;
    private int lookedcount;
    private String residual_asset;
    private int is_safe;
    private int sharecount;

    public String getVipotype() {
        return vipotype;
    }

    public void setVipotype(String vipotype) {
        this.vipotype = vipotype;
    }

    public int getDaycount() {
        return daycount;
    }

    public void setDaycount(int daycount) {
        this.daycount = daycount;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRandomnum() {
        return randomnum;
    }

    public void setRandomnum(String randomnum) {
        this.randomnum = randomnum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getVipendtime() {
        return vipendtime;
    }

    public void setVipendtime(String vipendtime) {
        this.vipendtime = vipendtime;
    }

    /**
     * 是否是vip 1：是 0：不是
     *
     * @return
     */
    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }


    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getSafecode() {
        return safecode;
    }

    public void setSafecode(String safecode) {
        this.safecode = safecode;
    }

    public int getDowncount() {
        return downcount;
    }

    public void setDowncount(int downcount) {
        this.downcount = downcount;
    }

    public int getLookcount() {
        return lookcount;
    }

    public void setLookcount(int lookcount) {
        this.lookcount = lookcount;
    }

    public int getLookedcount() {
        return lookedcount;
    }

    public void setLookedcount(int lookedcount) {
        this.lookedcount = lookedcount;
    }

    public String getResidual_asset() {
        return residual_asset;
    }

    public void setResidual_asset(String residual_asset) {
        this.residual_asset = residual_asset;
    }

    public int getIs_safe() {
        return is_safe;
    }

    public void setIs_safe(int is_safe) {
        this.is_safe = is_safe;
    }

    public int getSharecount() {
        return sharecount;
    }

    public void setSharecount(int sharecount) {
        this.sharecount = sharecount;
    }
}
