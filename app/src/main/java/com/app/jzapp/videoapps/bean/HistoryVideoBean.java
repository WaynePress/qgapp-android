package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 历史记录
 */
public class HistoryVideoBean implements MultiItemEntity {

    @Override
    public int getItemType() {
        return -0xff;
    }


    /**
     * lookid : 10001
     * vid : 10001
     * title : mv输送输送
     * pic : http://www.rbbc2.com:80/upload/17.jpg
     * looktime : 6699
     * createtime : 2018-11-30 15:16:17
     */


    private String time;
    List<TInmeData> data;

    @Override
    public String toString() {
        return "HistoryVideoBean{" +
                "time='" + time + '\'' +
                ", data=" + data +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TInmeData> getData() {
        return data;
    }

    public void setData(List<TInmeData> data) {
        this.data = data;
    }
}
