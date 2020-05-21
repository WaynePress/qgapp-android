package com.app.jzapp.videoapps.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TopBannerBean implements Parcelable {


    private String bar_content;//	广告条标题
    private String bar_prompt;//	弹出对话框提示文字
    private String bar_url;//	弹出对话框，点击安装后弹出的跳转网址

    public String getBar_content() {
        return bar_content;
    }

    public void setBar_content(String bar_content) {
        this.bar_content = bar_content;
    }

    public String getBar_prompt() {
        return bar_prompt;
    }

    public void setBar_prompt(String bar_prompt) {
        this.bar_prompt = bar_prompt;
    }

    public String getBar_url() {
        return bar_url;
    }

    public void setBar_url(String bar_url) {
        this.bar_url = bar_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bar_content);
        dest.writeString(this.bar_prompt);
        dest.writeString(this.bar_url);
    }

    public TopBannerBean() {
    }

    protected TopBannerBean(Parcel in) {
        this.bar_content = in.readString();
        this.bar_prompt = in.readString();
        this.bar_url = in.readString();
    }

    public static final Creator<TopBannerBean> CREATOR = new Creator<TopBannerBean>() {
        @Override
        public TopBannerBean createFromParcel(Parcel source) {
            return new TopBannerBean(source);
        }

        @Override
        public TopBannerBean[] newArray(int size) {
            return new TopBannerBean[size];
        }
    };
}
