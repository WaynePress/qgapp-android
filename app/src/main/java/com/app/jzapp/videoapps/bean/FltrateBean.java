package com.app.jzapp.videoapps.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class FltrateBean implements MultiItemEntity {
    /**
     * oid : 10011
     * otypename : 人气最高
     * param : [{"oid":10018,"otypename":"销量最多"}]
     */

    private int oid;
    private String otypename;
    private List<ParamBean> param;

    @Override
    public int getItemType() {
        return -0xff;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOtypename() {
        return otypename;
    }

    public void setOtypename(String otypename) {
        this.otypename = otypename;
    }

    public List<ParamBean> getParam() {
        return param;
    }

    public void setParam(List<ParamBean> param) {
        this.param = param;
    }


    public static class ParamBean implements MultiItemEntity {
        /**
         * oid : 10018
         * otypename : 销量最多
         */

        @Override
        public int getItemType() {
            return -0xff;
        }

        private int oid;
        private String otypename;


        public int getOid() {
            return oid;
        }

        public void setOid(int oid) {
            this.oid = oid;
        }

        public String getOtypename() {
            return otypename;
        }

        public void setOtypename(String otypename) {
            this.otypename = otypename;
        }
    }
}
