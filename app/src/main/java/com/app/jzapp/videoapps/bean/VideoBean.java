package com.app.jzapp.videoapps.bean;

import java.util.List;

public class VideoBean {


    /**
     * vid : 10001
     * title : mv输送输送
     * pic : http://www.rbbc2.com:80/1.png
     * content :
     * url : http://www.rbbc2.com:80/2.mp4
     * is_free : 0
     * createtime : 2018-10-24 07:47:26
     * is_collect : 1
     * is_vip : 0
     * starlist : [{"sid":10001,"uname":"成龙","pic":"","is_collect":0},{"sid":10002,"uname":"李连杰","pic":"","is_collect":0}]
     */

    private int vid;
    private String title;
    private String pic;
    private String content;
    private String url;
    private int is_free;
    private String createtime;
    private int is_collect;
    private int is_vip;
    private List<StarItemBean> starlist;
    private String otype;
    private List<Secondotype> secondotype;
    //    private String screenotype;
    private String hotcount;
    //是否可观看 1：是 0 不是
    private int is_look;
    //操作 0：未操作 1：点赞 2：点踩
    private int is_flag;

    private List<VideoUrlBean> m3u8;
    private int m3u8_default;
    private List<VideoUrlBean> v_download;
    private int v_download_default;
    private String share_text;
    private String banner_pic;
    private String banner_url;

    public String getBanner_pic() {
        return banner_pic;
    }

    public void setBanner_pic(String banner_pic) {
        this.banner_pic = banner_pic;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public int getIs_flag() {
        return is_flag;
    }

    public void setIs_flag(int is_flag) {
        this.is_flag = is_flag;
    }

    public String getHotcount() {
        return hotcount;
    }

    public void setHotcount(String hotcount) {
        this.hotcount = hotcount;
    }

    public int getIs_look() {
        return is_look;
    }

    public void setIs_look(int is_look) {
        this.is_look = is_look;
    }

    public List<Secondotype> getSecondotype() {
        return secondotype;
    }

    public void setSecondotype(List<Secondotype> secondotype) {
        this.secondotype = secondotype;
    }
//    public String getSecondotype() {
//        return secondotype;
//    }
//
//    public void setSecondotype(String secondotype) {
//        this.secondotype = secondotype;
//    }

//    public String getScreenotype() {
//        return screenotype;
//    }
//
//    public void setScreenotype(String screenotype) {
//        this.screenotype = screenotype;
//    }


    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public List<StarItemBean> getStarlist() {
        return starlist;
    }

    public void setStarlist(List<StarItemBean> starlist) {
        this.starlist = starlist;
    }


    public List<VideoUrlBean> getM3u8() {
        return m3u8;
    }

    public void setM3u8(List<VideoUrlBean> m3u8) {
        this.m3u8 = m3u8;
    }

    public int getM3u8_default() {
        return m3u8_default;
    }

    public void setM3u8_default(int m3u8_default) {
        this.m3u8_default = m3u8_default;
    }

    public List<VideoUrlBean> getV_download() {
        return v_download;
    }

    public void setV_download(List<VideoUrlBean> v_download) {
        this.v_download = v_download;
    }

    public int getV_download_default() {
        return v_download_default;
    }

    public void setV_download_default(int v_download_default) {
        this.v_download_default = v_download_default;
    }

    public String getShare_text() {
        return share_text;
    }

    public void setShare_text(String share_text) {
        this.share_text = share_text;
    }
}
