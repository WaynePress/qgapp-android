package com.app.jzapp.videoapps.bean;

import java.util.List;

public class OrderDataBean {

    /**
     * order : {"oid":10002,"ordernum":"1541993768619","payotype":"支付宝","title":"全站包月 CNY$30","price":"0.01","vipendtime":"2019-01-11","createtime":"2018-11-12"}
     * data : [{"oid":10002,"ordernum":"1541993768619","payotype":"支付宝","title":"全站包月 CNY$30","price":"0.01","vipendtime":"2019-01-11","createtime":"2018-11-12"},{"oid":10001,"ordernum":"1541833889112","payotype":"支付宝","title":"全站包月 CNY$30","price":"0.03","vipendtime":"2018-12-15","createtime":"2018-11-10"}]
     */

    private OrderBean order;
    private List<OrderBean> data;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<OrderBean> getData() {
        return data;
    }

    public void setData(List<OrderBean> data) {
        this.data = data;
    }


}
