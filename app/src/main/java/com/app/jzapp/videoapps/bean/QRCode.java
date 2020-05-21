package com.app.jzapp.videoapps.bean;

public class QRCode {

    /**
     * qrcode : http://www.rbbc2.com:80/assets/qrcodes/1541923148085.png
     * text : adwawaadwa
     */

    private String qrcode;
    private String pic;
    private String text;
    private String bgpic;

    public String getBgpic() {
        return bgpic;
    }

    public void setBgpic(String bgpic) {
        this.bgpic = bgpic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
