package com.app.jzapp.videoapps.event;

/**
 * 绑定手机成功
 */
public class BindAccountSuccessedEvent {
    String phone;

    public BindAccountSuccessedEvent(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
