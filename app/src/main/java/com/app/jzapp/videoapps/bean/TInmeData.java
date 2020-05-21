package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2019/1/7 0007.
 */

public class TInmeData  implements MultiItemEntity {
    @Override
    public int getItemType() {
        return -0xff;
    }
    private int lookid;

    private int vid;

    private String title;

    private String pic;

    private String looktime;

    private String createtime;

    @Override
    public String toString() {
        return "TInmeData{" +
                "lookid=" + lookid +
                ", vid=" + vid +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", looktime='" + looktime + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }

    public int getLookid() {
        return lookid;
    }

    public void setLookid(int lookid) {
        this.lookid = lookid;
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

    public String getLooktime() {
        return looktime;
    }

    public void setLooktime(String looktime) {
        this.looktime = looktime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
