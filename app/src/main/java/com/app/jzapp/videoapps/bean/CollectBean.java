package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CollectBean implements MultiItemEntity {
    @Override
    public int getItemType() {
        return -0xff;
    }

    /**
     * cid : 10001
     * oid : 10001
     * name : 成龙
     * pic : 1.png
     */

    private int cid;
    private int oid;
    private String name;
    private String pic;
    //是否限免 1：限免 0：不限免
    private int is_free;

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
