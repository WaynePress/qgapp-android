package com.app.jzapp.videoapps;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseActivity;

/**
 * Created by Administrator on 2018/12/28 0028.
 */

public class CS extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    protected int setLayoutId() {
        return R.layout.current_project;
    }

}
