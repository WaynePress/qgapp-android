package com.app.jzapp.videoapps.event;

/**
 * 筛选条件
 */
public class FiltrateEvent {
    private String screenotype;

    public FiltrateEvent(String screenotype) {
        this.screenotype = screenotype;
    }

    public String getScreenotype() {
        return screenotype;
    }

    public void setScreenotype(String screenotype) {
        this.screenotype = screenotype;
    }
}
