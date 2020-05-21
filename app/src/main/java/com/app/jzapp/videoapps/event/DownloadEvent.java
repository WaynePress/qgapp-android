package com.app.jzapp.videoapps.event;

public class DownloadEvent {
    int flay;
    String url;
    String filePath;

    public DownloadEvent(int flay, String url, String filePath) {
        this.flay = flay;
        this.url = url;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFlay() {
        return flay;
    }

    public void setFlay(int flay) {
        this.flay = flay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
