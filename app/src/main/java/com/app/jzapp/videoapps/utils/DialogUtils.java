package com.app.jzapp.videoapps.utils;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.app.video.videoapps.R;


/**
 * 功能：弹对话框的工具
 */
public class DialogUtils {
    private AlertDialog.Builder builder;
    private AlertDialog dialog = null;

    /**
     * @param context
     * @param view
     * @param cancel  点击对话框外部是否消失 false不消失
     * @param theme   对话框样式
     * @return
     */
    public AlertDialog showDialog(Context context, View view, int theme, boolean cancel) {
        if (builder == null) {
            if (Build.VERSION.SDK_INT < 20)
                builder = new AlertDialog.Builder(context);
            else
                builder = new AlertDialog.Builder(context, theme);
        }
//        Window window = ((BaseActivity) context).getWindow();
//        window.setBackgroundDrawable(new BitmapDrawable());
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
        return dialog;
    }

    public AlertDialog showDialog(Context context, View view, boolean cancel) {
        return showDialog(context, view, R.style.Theme_Light_NoTitle_Dialog, cancel);
    }
}
