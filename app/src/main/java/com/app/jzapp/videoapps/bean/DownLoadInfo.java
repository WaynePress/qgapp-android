package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DownLoadInfo extends DownLoadBean implements MultiItemEntity {

    @Override
    public int getItemType() {
        return -0xff;
    }

    public DownLoadInfo(Long id, String url, String name, String filePath, String iamgeUrl, int status) {
        super(id, url, name, filePath, iamgeUrl, status);
    }

    private long size;   //文件的总尺寸
    private long downloadLocation; // 下载的位置(就是当前已经下载过的size，也是断点的位置)

    @Override
    public String toString() {
        return "DownLoadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", size=" + size +
                ", iamgeUrl='" + iamgeUrl + '\'' +
                ", downloadLocation=" + downloadLocation +
                ", status=" + status +
                '}';
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(long downloadLocation) {
        this.downloadLocation = downloadLocation;
    }
}
