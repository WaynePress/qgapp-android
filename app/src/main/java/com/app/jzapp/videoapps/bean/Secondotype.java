package com.app.jzapp.videoapps.bean;

/**
 * Created by Administrator on 2019/1/7 0007.
 */

public class Secondotype {

    private int oid;
    private String otypename;

    @Override
    public String toString() {
        return "Secondotype{" +
                "oid=" + oid +
                ", otypename='" + otypename + '\'' +
                '}';
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOtypename() {
        return otypename;
    }

    public void setOtypename(String otypename) {
        this.otypename = otypename;
    }
}
