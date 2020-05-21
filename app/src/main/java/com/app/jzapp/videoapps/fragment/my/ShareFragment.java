package com.app.jzapp.videoapps.fragment.my;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.InviteBean;
import com.app.jzapp.videoapps.bean.QRCode;
import com.app.jzapp.videoapps.event.ShareSuccessedEvent;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.app.jzapp.videoapps.view.BottomMenuDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;
import static com.app.jzapp.videoapps.http.AppConfig.USERBEAN;

/**
 * 分享推广
 */
public class ShareFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.share_text)
    TextView share_text;

    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;

    private BottomMenuDialog dialog;
    private String shareUrl = AppConfig.getUrl() + "upload/share.html";

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_share;
    }


    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }

    @Override
    protected void initView() {
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.share));
        share_text.setText("【我的邀请码： " + USERBEAN.getInvitecode() + "】");
        Client.getApiService().getAppCon("2")
                .compose(RxsRxSchedulers.<BaseBean<QRCode>>io_main())
                .subscribe(new ApiServiceResult<QRCode>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<QRCode> bean) {
                        super.onNext(bean);

                        if (bean != null && bean.getData() != null)
                            setData(bean);
                    }
                });
    }

    private void setData(BaseBean<QRCode> bean) {
        GlideUtils.loadImagView(_mActivity, bean.getData().getQrcode(), ivQrcode);
    }

    @OnClick(R.id.tv_share)
    void share() {
//        if (dialog == null)
//            dialog = new BottomMenuDialog(_mActivity).
//                    setOnItemClickListener(new DialogItemListener<Integer>() {
//                        @Override
//                        public void onItemClick(Integer i) {
//                            switch (i) {
//                                case 0:
//                                    shareWeb(SHARE_MEDIA.WEIXIN);
////                                    share666();
//                                    break;
//                                case 1:
//                                    shareWeb(SHARE_MEDIA.WEIXIN_CIRCLE);
////                                    new ShareUtils(_mActivity).shareWeChatFriend(getString(R.string.app_name), shareUrl, TEXT, null);
//                                    break;
//                                case 2:
//                                    shareWeb(SHARE_MEDIA.QQ);
////                                    new ShareUtils(_mActivity).shareQQFriend(getString(R.string.app_name), shareUrl, TEXT, null);
//                                    break;
//                            }
//                        }
//                    }).buildShare();
//        dialog.show();
        share(getString(R.string.app_name)+"  "+getString(R.string.share_content));
    }



    private void share(String text) {
        Client.getApiService().getInviteUrl(TOKEN)
                .compose(RxsRxSchedulers.<BaseBean<InviteBean>>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult<InviteBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<InviteBean> bean) {
                        super.onNext(bean);
                        if (bean != null && "0".equals(bean.code)) {
                            String invitetext = bean.getData().getInvitetext();
                            ClipboardManager cm = (ClipboardManager) _mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("Label", ""+invitetext);
                            // 将ClipData内容放到系统剪贴板里。
                            cm.setPrimaryClip(mClipData);
                            ToastUtils.showLongToast(R.string.copy_invite_code);
                        }
                    }
                });

//        startActivity(IntentUtils.getShareTextIntent(text));
    }

    private void shareSuccessed() {
        Client.getApiService().share(TOKEN)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        super.onNext(bean);
                        EventBus.getDefault().post(new ShareSuccessedEvent());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
