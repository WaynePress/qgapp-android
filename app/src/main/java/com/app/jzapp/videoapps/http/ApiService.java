package com.app.jzapp.videoapps.http;

import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.AssetDetailBean;
import com.app.jzapp.videoapps.bean.AssetsBean;
import com.app.jzapp.videoapps.bean.CollectBean;
import com.app.jzapp.videoapps.bean.FltrateBean;
import com.app.jzapp.videoapps.bean.HistoryVideoBean;
import com.app.jzapp.videoapps.bean.InviteBean;
import com.app.jzapp.videoapps.bean.LoginBean;
import com.app.jzapp.videoapps.bean.OrderDataBean;
import com.app.jzapp.videoapps.bean.QRCode;
import com.app.jzapp.videoapps.bean.StarBean;
import com.app.jzapp.videoapps.bean.StarItemBean;
import com.app.jzapp.videoapps.bean.TopBannerBean;
import com.app.jzapp.videoapps.bean.TypeBean;
import com.app.jzapp.videoapps.bean.UserBean;
import com.app.jzapp.videoapps.bean.UserQrcodeBean;
import com.app.jzapp.videoapps.bean.VideoBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.bean.WithdrawalRecordBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    /**
     * 随机账号接口
     *
     * @param os 手机类型 1：ios 2：安卓
     * @return
     */
    @POST("api/v110/user/randomUser")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> randomUser(@Field("os") String os);

    /**
     * 获取用户信息接口
     *
     * @param token 用户token
     * @return
     */
    @POST("api/v110/user/getUserInfo")
    @FormUrlEncoded
    Observable<BaseBean<UserBean>> getUserInfo(@Field("token") String token);

    /**
     * 重新登录
     *
     * @return
     */
    @POST("{url}")
    Observable<BaseBean<String>> getToken(@Path(value = "url", encoded = true) String url);

    /**
     * 重新登录
     *
     * @return
     */
    @POST("api/v110/user/doLogin")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> doLogin(@Field("str") String str);

    /**
     * 绑定/找回手机号
     *
     * @param map
     * @return
     */
    @POST("api/v110/user/doUserMobile")
    @FormUrlEncoded
    Observable<BaseBean> doUserMobile(@FieldMap Map<String, String> map);

    /**
     * 登录
     *
     * @param map
     * @return
     */
    @POST("api/v110/user/switchNumber")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> login(@FieldMap Map<String, String> map);

    /**
     * 登录
     *
     * @param map
     * @return
     */
    @POST("api/v110/user/getVerifycode")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> getVerifycode(@FieldMap Map<String, String> map);

    /**
     * 安全码设置
     *
     * @param token
     * @param code
     * @return
     */
    @POST("api/v110/user/doSafeCode")
    @FormUrlEncoded
    Observable<BaseBean> doSafeCode(@Field("token") String token, @Field("code") String code);

    /**
     * 邀请码
     *
     * @param token
     * @param invitecode
     * @return
     */
    @POST("api/v110/user/subInviteCode")
    @FormUrlEncoded
    Observable<BaseBean> subInviteCode(@Field("token") String token, @Field("invitecode") String invitecode);

    /**
     * 获取“推广地址信息”复制到剪切板中
     * @param token
     * @return
     */
    @POST("api/v110/user/getInviteUrl")
    @FormUrlEncoded
    Observable<BaseBean<InviteBean>> getInviteUrl(@Field("token") String token);

    /**
     * 确认密码接口
     *
     * @param token
     * @param password
     * @return
     */
    @POST("api/v1/user/confirmPwd")
    @FormUrlEncoded
    Observable<BaseBean> confirmPwd(@Field("token") String token, @Field("password") String password);

    /**
     * 更新密码接口
     *
     * @param token
     * @param password
     * @param newpassword
     * @return
     */
    @POST("api/v1/user/updatePwd")
    @FormUrlEncoded
    Observable<BaseBean> updatePwd(@Field("token") String token, @Field("password") String password, @Field("newpassword") String newpassword);

    /**
     * 忘记密码
     *
     * @param number
     * @param password
     * @param oldpassword
     * @return
     */
    @POST("api/v1/user/forgetPwd")
    @FormUrlEncoded
    Observable<BaseBean> forgetPwd(@Field("number") String number, @Field("password") String password, @Field("oldpassword") String oldpassword);


    /**
     * 获取顶部“小导航条”广告信息
     * @return
     */

    @POST("api/v110/user/getTopBanner")
    @FormUrlEncoded
    Observable<BaseBean<TopBannerBean>> getTopBanner(@Field("token") String toke);

    /**
     * 获取首页mv/视频上部导航分类
     *
     * @param otype 分类 1：mv 5：视频
     * @return
     */
    @POST("api/v110/otype/getListOtype")
    @FormUrlEncoded
    Observable<BaseBean<List<TypeBean>>> getListOtype(@Field("otype") String otype);

    /**
     * 获取mv/视频分类列表 接口
     *
     * @param otype 分类 1：mv 5：视频
     * @return
     */
    @POST("api/v1/otype/getVideoOtype")
    @FormUrlEncoded
    Observable<BaseBean<List<TypeBean>>> getVideoOtype(@Field("otype") String otype, @Field("page") String page, @Field("pageSize") String pageSize);

    /**
     * 获取视频列表
     * <p>
     * 1：mv 5：视频
     *
     * @return
     */
    @POST("api/v110/video/getVideoList")
    @FormUrlEncoded
    Observable<BaseBean<List<VideoItemBean>>> getVideoList(@FieldMap Map<String, String> map);

    /**
     * 获取相关影片-随机
     *
     * @return
     */
    @POST("api/v1/video/getRandomVideoList")
    @FormUrlEncoded
    Observable<BaseBean<List<VideoItemBean>>> getRandomVideoList(@Field("token") String token);

    /**
     * 加入收藏接口
     *
     * @param otype 收藏对象类型 1：明星 5：mv 10：视频
     * @return
     */
    @POST("api/v1/collect/addCollect")
    @FormUrlEncoded
    Observable<BaseBean> addCollect(@Field("token") String token, @Field("oid") String oid, @Field("otype") String otype);

    /**
     * 取消收藏接口
     *
     * @param otype 收藏对象类型 1：明星 5：mv 10：视频
     * @return
     */
    @POST("api/v1/collect/delCollect")
    @FormUrlEncoded
    Observable<BaseBean> delCollect(@Field("token") String token, @Field("oid") String oid, @Field("otype") String otype);

    /**
     * 收藏列表接口
     *
     * @param otype 收藏对象类型 1：明星 5：mv 10：视频
     * @return
     */
    @POST("api/v1/collect/getCollectList")
    @FormUrlEncoded
    Observable<BaseBean<List<CollectBean>>> getCollectList(@Field("token") String token, @Field("otype") String otype, @Field("page") String page);

    /**
     * 获取明星列表
     * <p>
     * 1：mv 5：视频
     *
     * @return
     */
    @POST("api/v1/star/getStarList")
    @FormUrlEncoded
    Observable<BaseBean<List<StarItemBean>>> getStarList(@FieldMap Map<String, String> map);

    /**
     * 明星简介
     * <p>
     * 1：mv 5：视频
     *
     * @return
     */
    @POST("api/v110/star/getStarInfoBySID")
    @FormUrlEncoded
    Observable<BaseBean<StarBean>> getStarInfo(@Field("token") String token, @Field("sid") String sid, @Field("page") String page);

    /**
     * 获取视频详情
     *
     * @return
     */
    @POST("api/v110/video/getVideoDetails")
    @FormUrlEncoded
    Observable<BaseBean<VideoBean>> getVideoDetails(@Field("token") String token, @Field("vid") String vid);

    /**
     * 消耗观看次数
     *
     * @return
     */
    @POST("api/v110/video/doLook")
    @FormUrlEncoded
    Observable<BaseBean> doLook(@Field("token") String token, @Field("looktime") String looktime, @Field("vid") int vid, @Field("flag") String flag);

    /**
     * 视频点踩/点赞接口
     *
     * @return
     */
    @POST("api/v110/video/doFlag")
    @FormUrlEncoded
    Observable<BaseBean> doFlag(@Field("token") String token, @Field("vid") int vid, @Field("is_flag") int is_flag);

    /**
     * 获取筛选条件
     *
     * @return
     */
    @POST("api/v1/otype/getScreenOtype")
    @FormUrlEncoded
    Observable<BaseBean<List<FltrateBean>>> getScreenOtype(@Field("otype") String otype);

    /**
     * 用户求片接口
     *
     * @return
     */
    @POST("api/v110/msg/subSeekVideo")
    @FormUrlEncoded
    Observable<BaseBean> subSeekVideo(@Field("token") String token, @Field("content") String content);

    /**
     * 引导页内容接口
     *
     * @param os 1:ios 2:andriod
     * @return
     */
    @POST("api/v1/app/getAppCon")
    @FormUrlEncoded
    Observable<BaseBean<QRCode>> getAppCon(@Field("os") String os);

    /**
     * 获取服务器状态
     * @return
     */
    @GET("api/v1/app/getStatus")
    Observable<BaseBean> getStatus();

    /**
     * 获取订单 方案管理接口
     *
     * @return
     */
    @POST("api/v1/order/getOrderList")
    @FormUrlEncoded
    Observable<BaseBean<OrderDataBean>> getOrderList(@Field("token") String token, @Field("page") int page);

    /**
     * 下订单
     *
     * @param token
     * @param num
     * @param price
     * @param payotype 订单类型 1 支付宝 2：微信
     * @return
     */
    @POST("api/v110/order/subOrder")
    @FormUrlEncoded
    Observable<BaseBean<String>> subOrder(@Field("token") String token, @Field("num") int num, @Field("price") String  price, @Field("payotype") int payotype, @Field("vipotype") int vipotype);

    /**
     * 月季年卡金额
     */
    @POST("api/v110/app/getVip")
    @FormUrlEncoded
    Observable<BaseBean<String>> getVip(@Field("token") String token);


    /**
     * 下订单
     *
     * @return
     */
    @POST("api/v110/order/payOrder")
    @FormUrlEncoded
    Observable<BaseBean<String>> payOrder(@Field("token") String token, @Field("ordernum") String ordernum);
    /**
     *
     */


    /**
     * 用户分享增加天数
     *
     * @return
     */
    @POST("api/v110/app/share")
    @FormUrlEncoded
    Observable<BaseBean> share(@Field("token") String token);

    /**
     * 提现-获取资产
     *
     * @return
     */
    @POST("api/v110/asset/getUserAssetInfo")
    @FormUrlEncoded
    Observable<BaseBean<AssetsBean>> getUserAssetInfo(@Field("token") String token);

    /**
     * 提交提现申请接口
     *
     * @return
     */
    @POST("api/v110/asset/subUserWithdraw")
    @FormUrlEncoded
    Observable<BaseBean> subUserWithdraw(@FieldMap Map<String, String> map);

    /**
     * 提交提现申请接口
     *
     * @return
     */
    @POST("api/v110/asset/getUserWithdraw")
    @FormUrlEncoded
    Observable<BaseBean<List<WithdrawalRecordBean>>> getUserWithdraw(@Field("token") String token, @Field("page") int page, @Field("pageSize") String pageSize);

    /**
     * 收益明细
     *
     * @return
     */
    @POST("api/v110/asset/getUserAssetDetail")
    @FormUrlEncoded
    Observable<BaseBean<List<AssetDetailBean>>> getUserAssetDetail(@Field("token") String token, @Field("page") int page, @Field("pageSize") String pageSize);

    /**
     * 获取用户二维码信息接口
     *
     * @return
     */
    @POST("api/v110/user/getUserQrcode")
    @FormUrlEncoded
    Observable<BaseBean<UserQrcodeBean>> getUserQrcode(@Field("token") String token);

    /**
     * 获取播放历史接口
     *
     * @return
     */
    @POST("api/v110/video/getLookLogs")
    @FormUrlEncoded
    Observable<BaseBean<List<HistoryVideoBean>>> getLookLogs(@Field("token") String token, @Field("page") int page);

    /**
     * 获取播放历史接口
     *
     * @return
     */
    @POST("api/v110/video/doDown")
    @FormUrlEncoded
    Observable<BaseBean> doDown(@Field("token") String token, @Field("vid") int vid, @Field("is_down") String is_down);

    /**
     * 获取播放历史接口
     *
     * @return
     */
    @POST("api/v110/app/doClick")
    @FormUrlEncoded
    Observable<BaseBean> doClick(@Field("token") String token);

    /**
     * 用户反馈
     *
     * @return
     */
    @POST("api/v110/msg/subVideoTrouble")
    @FormUrlEncoded
    Observable<BaseBean> subVideoTrouble(@Field("token") String token, @Field("content") String content, @Field("vid") String vid);

}
