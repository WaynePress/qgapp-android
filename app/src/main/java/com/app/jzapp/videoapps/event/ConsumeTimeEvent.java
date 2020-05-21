package com.app.jzapp.videoapps.event;

/**
 * 消耗观看次数
 */
public class ConsumeTimeEvent {
    private String flag;

    public ConsumeTimeEvent(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
