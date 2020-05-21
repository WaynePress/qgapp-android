package com.app.jzapp.videoapps.http;

import com.app.jzapp.videoapps.bean.DownLoadBean;
import com.app.jzapp.videoapps.bean.TopBannerBean;
import com.app.jzapp.videoapps.bean.UserBean;
import com.app.jzapp.videoapps.utils.SPUtils;

import java.util.List;

public class AppConfig {

    public static final String BASEURL = "http://youkuav.cc/";
    public static final String BASEURL1 = "http://youkuav.cc/";
    public static final String BASEURL2 = "http://youkuav.cc/";
    public static final String CAR_CROWD  = "http://youkuav.cc/web/index.php";
    public static final String SHAREIMAGEURL = "upload/logo.png";

    public static final String KEY = "lutube110";

    public static final String SP_NAME = "videoApp";

    public static final String SP_TOKEN = "toekn";
    public static final String SP_USERID = "UserId";
    public static final String SP_PHONE = "phone";
    public static final String SP_SB1NONBER = "SB1NONBER";
    public static final String SP_SB2NONBER = "SB2NONBER";
    public static final String SP_SB3NONBER = "SB3NONBER";
    public static final String SP_BASE_URL = "baseUrl";
    //        public static final String WX_ID = "wx693414e20f174a75";
    //public static final String WX_SECERT = "5cfc67aa9d27d05acec4e47fe27d7da4";
//    public static final String WX_ID = "wx547df76cd7e236bc";
//    public static final String WX_SECERT = "6bec9afac4ad04e0a550bd9864712d15";
//    public static final String QQ_ID = "1106869970";
//    public static final String QQ_SECERT = "or8p92PLCS3Ks9eb";
//    public static final String WEIBO_ID = "2469904608";
//    public static final String WEIBO_SECRET = "e3d19526e3fd4997141bdb628231fd69";
    public static final String UMENGT_KEY = "5cd24c830cafb2aa17000217";
    public static final String MQ = "9b1fc8e48b9437ae5c966e137aabf589";

    public static String TOKEN;
    public static String USERID;
    public static String PHONE;
    public static TopBannerBean topBannerBean;
    /**
     * 用户信息
     */
    public static UserBean USERBEAN;

    public static final String PSW_SWITCH = "psw_switch";
    public static final String AUTOPLAY_SWITCH = "autoplay_switch";
    public static final String PSW = "psw";


    // Answer to the Ultimate Question of Life, The Universe, and Everything is 42
    public static final int WAIT = 42;       //等待
    public static final int PREPARE = 43;    //准备
    public static final int LOADING = 44;    //下载中
    public static final int PAUSE = 45;      //暂停
    public static final int COMPLETE = 46;   //完成
    public static final int FAIL = 47;       //失败

    public static List<DownLoadBean> sDownLoadBeans;

    public static String getUrl(){
        return new SPUtils(SP_NAME).getString(AppConfig.SP_BASE_URL,BASEURL);
    }

    public static void setTopBannerBean(TopBannerBean topBannerBean) {
        AppConfig.topBannerBean = topBannerBean;
    }

    public static TopBannerBean getTopBannerBean() {
        return topBannerBean;
    }
}
