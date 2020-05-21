package com.app.jzapp.videoapps.bean;

import java.util.List;

public class StarBean {


    /**
     * star : {"sid":10001,"uname":"成龙","pic":"","count":0}
     * dataTmp : [{"vid":10002,"title":"特殊儿童","pic":"http://www.rbbc2.com:80/2.png","url":"http://www.rbbc2.com:80/2.mp4","is_free":1,"is_collect":0},{"vid":10001,"title":"mv输送输送","pic":"http://www.rbbc2.com:80/1.png","url":"http://www.rbbc2.com:80/2.mp4","is_free":0,"is_collect":1}]
     */

    private Star star;
    private List<VideoItemBean> dataTmp;

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public List<VideoItemBean> getDataTmp() {
        return dataTmp;
    }

    public void setDataTmp(List<VideoItemBean> dataTmp) {
        this.dataTmp = dataTmp;
    }

    public static class Star {
        /**
         * sid : 10001
         * uname : 成龙
         * pic :
         * count : 0
         */

        private int sid;
        private String uname;
        private String pic;
        private int count;
        private int usercount;
        private int is_collect;

        public int getUsercount() {
            return usercount;
        }

        public void setUsercount(int usercount) {
            this.usercount = usercount;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
