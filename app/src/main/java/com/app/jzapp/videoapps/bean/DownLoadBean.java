package com.app.jzapp.videoapps.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DownLoadBean {
    //@Id：主键，通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    @Id(autoincrement = true)
    protected Long id;

    protected String url;

    protected String name;

    protected String filePath;

    protected String iamgeUrl;

    protected int status;

    @Generated(hash = 1153734252)
    public DownLoadBean(Long id, String url, String name, String filePath,
            String iamgeUrl, int status) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.filePath = filePath;
        this.iamgeUrl = iamgeUrl;
        this.status = status;
    }

    @Generated(hash = 600345743)
    public DownLoadBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIamgeUrl() {
        return this.iamgeUrl;
    }

    public void setIamgeUrl(String iamgeUrl) {
        this.iamgeUrl = iamgeUrl;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DownLoadBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", iamgeUrl='" + iamgeUrl + '\'' +
                ", status=" + status +
                '}';
    }
}
