package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class OrderBean implements MultiItemEntity {
    /**
     * oid : 10002
     * ordernum : 1541993768619
     * payotype : 支付宝
     * title : 全站包月 CNY$30
     * price : 0.01
     * vipendtime : 2019-01-11
     * createtime : 2018-11-12
     */

    private int oid;
    private String ordernum;
    private String payotype;
    private String title;
    private String price;
    private String vipendtime;
    private String createtime;

    @Override
    public int getItemType() {
        return -0xff;
    }


    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getPayotype() {
        return payotype;
    }

    public void setPayotype(String payotype) {
        this.payotype = payotype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVipendtime() {
        return vipendtime;
    }

    public void setVipendtime(String vipendtime) {
        this.vipendtime = vipendtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
