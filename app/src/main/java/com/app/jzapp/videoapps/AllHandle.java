package com.app.jzapp.videoapps;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2019/1/11 0011.
 */

public class AllHandle extends Handler {
    private HandleMsgListener listener;
    private String Tag = AllHandle.class.getSimpleName();

    //使用单例模式创建GlobalHandler
    private AllHandle(){
        Log.e(Tag,"GlobalHandler创建");
    }



    private static class Holder{
        private static final AllHandle HANDLER = new AllHandle();
    }

    public static AllHandle getInstance(){
        return Holder.HANDLER;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getHandleMsgListener() != null){
            getHandleMsgListener().handleMsg(msg);
        }else {
            Log.e(Tag,"请传入HandleMsgListener对象");
        }
    }

    public interface HandleMsgListener{
        void handleMsg(Message msg);
    }

    public void setHandleMsgListener(HandleMsgListener listener){
        this.listener = listener;
    }

    public HandleMsgListener getHandleMsgListener(){
        return listener;
    }


}
