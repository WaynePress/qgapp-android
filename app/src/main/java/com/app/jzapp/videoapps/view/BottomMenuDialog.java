package com.app.jzapp.videoapps.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.listener.DialogItemListener;

import java.util.List;

/**
 * Created by soul on 2016/11/10.
 */

public class BottomMenuDialog<T> extends Dialog {

    private Context context;
    private List<T> list;

    private boolean canCancel = true;
    private boolean shadow = true;
    private boolean firstCanChoose = true;

    private DialogItemListener mListener;

    BottomMenuDialog dialog;

    public BottomMenuDialog(Context context) {
        super(context);
        this.context = context;
    }

    public BottomMenuDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public BottomMenuDialog(Context context, boolean firstCanChoose) {
        super(context);
        this.context = context;
        this.firstCanChoose = firstCanChoose;
    }

    public BottomMenuDialog setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
        return this;
    }

    public BottomMenuDialog setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public BottomMenuDialog setData(List<T> list) {
        this.list = list;
        return this;
    }

    public BottomMenuDialog setOnItemClickListener(DialogItemListener listener) {
        this.mListener = listener;
        return this;
    }


    public BottomMenuDialog buildShare() {
        dialog = new BottomMenuDialog(
                context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);

        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_share, null);

        view.findViewById(R.id.tv_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(0);
                }
            }
        });
        view.findViewById(R.id.tv_pyq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(1);
                }
            }
        });
        view.findViewById(R.id.tv_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(2);
                }
            }
        });

        TextView cancle = view.findViewById(R.id.tv_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(canCancel);
        dialog.setCancelable(canCancel);
        return dialog;
    }

    public BottomMenuDialog buildWithdrawDeposit() {
        dialog = new BottomMenuDialog(
                context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);

        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_withdraw_deposit, null);

        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(canCancel);
        dialog.setCancelable(canCancel);
        return dialog;
    }
}
