package com.app.jzapp.videoapps.http;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.app.video.videoapps.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class DialogTransformer<T> {
    private static final String DEFAULT_MSG = "数据加载中...";

    private Context mContext;
    private String msg;
    private boolean cancelable;
    //    private ProgressDialog progressDialog;
    private Dialog dialog;

    public DialogTransformer(Context mContext) {
        this(mContext, DEFAULT_MSG);
    }

    public DialogTransformer(Context mContext, String msg) {
        this(mContext, msg, false);
    }

    public DialogTransformer(Context mContext, String msg, boolean cancelable) {
        this.mContext = mContext;
        this.msg = msg;
        this.cancelable = cancelable;
    }

    public <T> ObservableTransformer<T, T> transformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(final Observable<T> upstream) {

                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog = createLoadingDialog(mContext, cancelable);
                        dialog.show();
                    }
                }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Observable.timer(500, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.cancel();
                                        }
                                    }
                                });
                    }
                });
            }
        };
    }

    public void dismiss() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public Dialog createLoadingDialog(Context context, boolean cancelable) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = v.findViewById(R.id.dialog_view);// 加载布局
//        // main.xml中的ImageView
//        ImageView spaceshipImage = v.findViewById(R.id.img);
//        // 加载动画
//        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, R.anim.loading_animation);
//        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(cancelable);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }


}
