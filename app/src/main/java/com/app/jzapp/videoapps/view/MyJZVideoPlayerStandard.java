package com.app.jzapp.videoapps.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.jzapp.videoapps.MyApplication;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.DownVideoEvent;
import com.app.jzapp.videoapps.bean.SetVideoEvent;
import com.app.jzapp.videoapps.bean.VideoBean;
import com.app.jzapp.videoapps.event.ConsumeTimeEvent;
import com.app.jzapp.videoapps.event.VideoProgressEvent;
import com.app.jzapp.videoapps.utils.BarUtils;
import com.app.jzapp.videoapps.utils.ConvertUtils;
import com.app.jzapp.videoapps.utils.PhoneUtils;
import com.app.jzapp.videoapps.utils.SizeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerStandard;

public class MyJZVideoPlayerStandard extends JZVideoPlayerStandard {
    /**
     * 是不是只能观看一分钟
     */
    public boolean isLimet;


    private VideoBean videoBean;
    public static long position;

    public boolean islocked;

    ImageView ivLock, ivSet, ivDown;
    View img_touming;

    public MyJZVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setBiao_chao(int biao_chao) {
        MyApplication.biao_chao = biao_chao;
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void init(Context context) {
        super.init(context);
        ivLock = findViewById(R.id.iv_lock);
        ivSet = findViewById(R.id.iv_set);
        ivDown = findViewById(R.id.iv_down);
        img_touming = findViewById(R.id.img_touming);
        ivLock.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        ivDown.setOnClickListener(this);
        mRetryLayout.setVisibility(INVISIBLE);
        topContainer.setVisibility(INVISIBLE);
//        setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
//                View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_standard_my;
    }


    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
        ivLock.setVisibility(GONE);
        ivDown.setVisibility(GONE);
        ivSet.setVisibility(GONE);
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        ivLock.setVisibility(VISIBLE);
        ivDown.setVisibility(VISIBLE);
        ivSet.setVisibility(VISIBLE);
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
        ivLock.setVisibility(GONE);
        ivDown.setVisibility(GONE);
        ivSet.setVisibility(GONE);
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        ivLock.setVisibility(VISIBLE);
        ivDown.setVisibility(VISIBLE);
        ivSet.setVisibility(VISIBLE);
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        ivLock.setVisibility(GONE);
        ivDown.setVisibility(GONE);
        ivSet.setVisibility(GONE);
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
        ivLock.setVisibility(GONE);
        ivDown.setVisibility(GONE);
        ivSet.setVisibility(GONE);
        if (MyApplication.biao_chao == 0) {
            img_touming.setVisibility(VISIBLE);
        } else if (MyApplication.biao_chao == 1) {
            img_touming.setVisibility(GONE);
        }
    }

    @Override
    public void dissmissControlView() {
        super.dissmissControlView();
        post(new Runnable() {
            @Override
            public void run() {
                if (ivLock.getVisibility() == VISIBLE) {
                    ivLock.setVisibility(GONE);
                }
            }
        });
    }

    @Override
    public void setProgressAndText(int progress, long position, long duration) {
        this.position = position;
        if (videoBean != null){
//          限免\会员\可以观看
            if (videoBean.getIs_free() == 1 || videoBean.getIs_vip() == 1 || videoBean.getIs_look() == 1) {
                isLimet = false;
            } else {
                isLimet = true;
            }
        }
//        Log.d(TAG, "=====setProgressAndText: progress=" + progress + " position=" + position + " duration=" + duration);
        super.setProgressAndText(progress, position, duration);
        if (progress != 0) bottomProgressBar.setProgress(progress);
        if (isLimet && position / 1000 / 60 >= 1) {
            EventBus.getDefault().post(new VideoProgressEvent());
            JZMediaManager.pause();
            onStatePause();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            //是否可观看 1：是 0 不是
//            if (videoBean.getIs_look() == 0) {
//                return;
//            }
        } else if (i == cn.jzvd.R.id.thumb) {
//            if (videoBean.getIs_look() == 0) {
//                return;
//            }
        } else if (i == R.id.iv_lock) {
            if (islocked) {
                ivLock.setImageResource(R.drawable.ic_lock_open_black_24dp);
            } else {
                dissmissControlView();
                ivLock.setImageResource(R.drawable.ic_lock_outline_black_24dp);
            }
            islocked = !islocked;
        } else if (i == R.id.iv_set) {
            EventBus.getDefault().post(new SetVideoEvent());
        } else if (i == R.id.iv_down) {
            EventBus.getDefault().post(new DownVideoEvent());
        }else if (i == R.id.clarity) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.jz_layout_clarity, null);

            OnClickListener mQualityListener = new OnClickListener() {
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    onStatePreparingChangingUrl(index, getCurrentPositionWhenPlaying());
                    clarity.setText(JZUtils.getKeyFromDataSource(dataSourceObjects, currentUrlMapIndex));
                    for (int j = 0; j < layout.getChildCount(); j++) {//设置点击之后的颜色
                        if (j == currentUrlMapIndex) {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#fff85959"));
                        } else {
                            ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    if (clarityPopWindow != null) {
                        clarityPopWindow.dismiss();
                    }
                }
            };

            for (int j = 0; j < ((LinkedHashMap) dataSourceObjects[0]).size(); j++) {
                String key = JZUtils.getKeyFromDataSource(dataSourceObjects, j);
                TextView clarityItem = (TextView) View.inflate(getContext(), R.layout.jz_layout_clarity_item, null);
                clarityItem.setText(key);
                clarityItem.setTag(j);
                layout.addView(clarityItem, j);
                clarityItem.setOnClickListener(mQualityListener);
                if (j == currentUrlMapIndex) {
                    clarityItem.setTextColor(Color.parseColor("#fff85959"));
                }
            }

            clarityPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            clarityPopWindow.setContentView(layout);
            clarityPopWindow.showAsDropDown(clarity);
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int offsetX = clarity.getMeasuredWidth() / 3;
            int offsetY = layout.getMeasuredHeight() + clarity.getMeasuredHeight() ;
            clarityPopWindow.update(clarity, - offsetX, - offsetY, Math.round(layout.getMeasuredWidth() * 2), layout.getMeasuredHeight());
            return;
        }
        super.onClick(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (islocked) {
            ivLock.setVisibility(VISIBLE);
            return true;
        }


        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (mChangePosition) {
                        setSpeed(VISIBLE);
                    }
                    break;
            }
        }
        //增加小屏滑动功能
        tempCurrentScreen = currentScreen;
        currentScreen = SCREEN_WINDOW_FULLSCREEN;
        boolean touch = super.onTouch(v, event);
        currentScreen = tempCurrentScreen;
        tempCurrentScreen = -1;
        return touch;

    }
    int tempCurrentScreen;

    @Override
    public Dialog createDialogWithView(View localView) {
        if (tempCurrentScreen == SCREEN_WINDOW_FULLSCREEN ){
            return super.createDialogWithView(localView);
        }
        Dialog dialog = new Dialog(getContext(), R.style.jz_style_dialog_progress);
        dialog.setContentView(localView);
        Window window = dialog.getWindow();
        window.addFlags(Window.FEATURE_ACTION_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
//        localLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        localLayoutParams.y = (getMeasuredHeight() -SizeUtils.dp2px(155))/2+ BarUtils.getStatusBarHeight(getContext());
        window.setAttributes(localLayoutParams);
        return dialog;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        //调接口 消耗观看次数的
        consumeTime();
    }

    @Override
    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
        clarity.setVisibility(View.VISIBLE);//非全屏显示清晰度
    }

    private void consumeTime() {
        EventBus.getDefault().post(new ConsumeTimeEvent("0"));
    }

    public void setData(VideoBean videoBean) {
        this.videoBean = videoBean;
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        ViewGroup vp = (JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        MyJZVideoPlayerStandard fullPlayer = vp.findViewById(R.id.jz_fullscreen_id);
        fullPlayer.setData(videoBean);
    }

    @Override
    public void onSeekComplete() {
        super.onSeekComplete();
        setSpeed(INVISIBLE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        setSpeed(VISIBLE);
    }


    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, thumbImg, bottomPro, retryLayout);
        setSpeed(loadingPro);
    }

    private void setSpeed(int loadingPro) {
        loadingProgressBar.setVisibility(loadingPro);
        try {//动态增加播放速度
            if (loadingPro == VISIBLE) {
                RelativeLayout relativeLayout = (RelativeLayout) loadingProgressBar.getParent();
                TextView textView = relativeLayout.findViewById(R.id.tv_search);
                if (textView == null) {
                    textView = new TextView(getContext());
                    textView.setId(R.id.tv_search);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    textView.setLayoutParams(params);
                    textView.setPadding(0, ConvertUtils.dp2px(45),0,0);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    relativeLayout.addView(textView);
                }
                textView.setVisibility(loadingPro);
                speedTimerTask = new SpeedTimerTask();
                speedTimer = new Timer();
                totalRx = PhoneUtils.getTotalRxKB(1);
                speedTimer.schedule(speedTimerTask, 1000, 1000);
            } else {
                RelativeLayout relativeLayout = (RelativeLayout) loadingProgressBar.getParent();
                TextView textView = relativeLayout.findViewById(R.id.tv_search);
                if (textView != null) {
                    textView.setVisibility(loadingPro);
                }
                cancleSpeedTimer();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void cancleSpeedTimer() {
        if (speedTimer != null) {
            speedTimer.cancel();
        }
        if (speedTimerTask != null) {
            speedTimerTask.cancel();
        }
    }

    Timer speedTimer;
    SpeedTimerTask speedTimerTask;
    double totalRx;

    public class SpeedTimerTask extends TimerTask {
        @Override
        public void run() {
            post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout relativeLayout = (RelativeLayout) loadingProgressBar.getParent();
                    TextView textView = relativeLayout.findViewById(R.id.tv_search);
                    if (textView != null) {
                        double nowTotalRx =PhoneUtils.getTotalRxKB(1);
                        textView.setText(String.format("%.1fKB", (nowTotalRx - totalRx) + 0.5));
                        totalRx = nowTotalRx;
                    }

                }
            });

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancleSpeedTimer();
    }
}
