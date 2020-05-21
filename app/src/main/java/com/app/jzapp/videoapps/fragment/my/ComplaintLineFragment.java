package com.app.jzapp.videoapps.fragment.my;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

/**
 * MV番号求片
 */

public class ComplaintLineFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_send)
    TextView tvSend;

    public static ComplaintLineFragment newInstance() {
        ComplaintLineFragment fragment = new ComplaintLineFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_complaint_line;
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
        title.setText(getString(R.string.request_video));
    }

    @OnClick(R.id.tv_send)
    void send() {
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_video_id));
            return;
        }

        Client.getApiService().subSeekVideo(TOKEN, content)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        super.onNext(bean);
                        ToastUtils.showShortToast(bean.getMsg());
                        _mActivity.onBackPressed();
                    }
                });

    }
}
