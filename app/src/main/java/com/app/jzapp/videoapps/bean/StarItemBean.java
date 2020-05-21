package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class StarItemBean implements MultiItemEntity {
    int itemType = -0xff;
    /**
     * sid : 10001
     * uname : 成龙
     * pic :
     * is_collect : 0
     */

    private int sid;
    private String uname;
    private String pic;
    private int is_collect;

    //筛选的数据
    private FltrateBean fltrateBean;

    public FltrateBean getFltrateBean() {
        return fltrateBean;
    }

    public void setFltrateBean(FltrateBean fltrateBean) {
        this.fltrateBean = fltrateBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public static final int HEAD = 4;
    public static final int NORAML = 1;

    private int spanSize = 1;

    public StarItemBean() {

    }

    public StarItemBean(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }
}
