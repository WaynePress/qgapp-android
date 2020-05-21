package com.app.jzapp.videoapps.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TypeBean implements Parcelable {
    @Override
    public String toString() {
        return "TypeBean{" +
                "oid=" + oid +
                ", otypename='" + otypename + '\'' +
                ", pic='" + pic + '\'' +
                ", pics=" + pics +
                '}';
    }

    /**
     * oid : 10001
     * otypename : 综艺
     * pic :
     */

    private int oid;
    private String otypename;
    private String pic;
    private List<PicsBean> pics;

    protected TypeBean(Parcel in) {
        oid = in.readInt();
        otypename = in.readString();
        pic = in.readString();
    }

    public static final Creator<TypeBean> CREATOR = new Creator<TypeBean>() {
        @Override
        public TypeBean createFromParcel(Parcel in) {
            return new TypeBean(in);
        }

        @Override
        public TypeBean[] newArray(int size) {
            return new TypeBean[size];
        }
    };

    public List<PicsBean> getPics() {
        return pics;
    }

    public void setPics(List<PicsBean> pics) {
        this.pics = pics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(oid);
        parcel.writeString(otypename);
        parcel.writeString(pic);
    }

    public static class PicsBean {
        @Override
        public String toString() {
            return "PicsBean{" +
                    "title='" + title + '\'' +
                    ", pic='" + pic + '\'' +
                    ", url='" + url + '\'' +
                    ", urlotype=" + urlotype +
                    '}';
        }

        /**
         * title : 标题图片1
         * pic : http://www.rbbc2.com:80/assets/uploads/image/otype/2018/1109/1541757207610.jpg
         */

        private String title;
        private String pic;
        private String url;
        private int urlotype;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUrlotype() {
            return urlotype;
        }

        public void setUrlotype(int urlotype) {
            this.urlotype = urlotype;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
