package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class VideoItemBean implements MultiItemEntity {
    int itemType = -0xff;
    /**
     * vid : 10001
     * title : mv输送输送
     * pic : http://www.rbbc2.com:80/1.png
     * url : http://www.rbbc2.com:80/2.mp4
     * is_free : 0
     * is_collect : 1
     */

    private int vid;
    private int cid;
    private String title;
    private String pic;
    private String url;
    //是否限免 1：限免 0：不限免
    private int is_free;
    //是否收藏 1：已收藏 0：未收藏
    private int is_collect;
    private int otype;

    private String hotcount;
    private String videotime;
    private String createtime;

    private int urlotype;

    public int getUrlotype() {
        return urlotype;
    }

    public void setUrlotype(int urlotype) {
        this.urlotype = urlotype;
    }

    //筛选的数据
    private FltrateBean fltrateBean;

    public String getHotcount() {
        return hotcount;
    }

    public void setHotcount(String hotcount) {
        this.hotcount = hotcount;
    }

    public String getVideotime() {
        return videotime;
    }

    public void setVideotime(String videotime) {
        this.videotime = videotime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public FltrateBean getFltrateBean() {
        return fltrateBean;
    }

    public void setFltrateBean(FltrateBean fltrateBean) {
        this.fltrateBean = fltrateBean;
    }

    public int getOtype() {
        return otype;
    }

    public void setOtype(int otype) {
        this.otype = otype;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public static final int HEAD = 2;
    public static final int NORAML = 1;

    private int spanSize = 1;

    public VideoItemBean() {

    }

    public VideoItemBean(int itemType, int spanSize) {
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
