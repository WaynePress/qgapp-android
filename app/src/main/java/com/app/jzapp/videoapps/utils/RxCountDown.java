package com.app.jzapp.videoapps.utils;

import android.util.Log;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 实现倒计时功能
 * Created by Administrator on 2017/12/22.
 */

public class RxCountDown {
    private static FlowableSubscriber subscriber;
    private static boolean release;

    public static void clockButton(final TextView view, final int second) {
        release = false;
        //用来在执行子线程中的FlowableOnSubscribe的subscribe方法之前执行
        Consumer consumer = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                view.setClickable(false);
            }
        };

        //返回主线程执行
        subscriber = new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(second + 1);//需要接收,1秒一条,还有最后一个onComplete
            }

            @Override
            public void onNext(Integer i) {
                Log.d("FlowableSubscriber", "i==: " + i);
                view.setText(i + "s");
            }

            @Override
            public void onError(Throwable t) {
                Log.e("FlowableSubscriber", "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d("FlowableSubscriber", "onComplete: ");
                view.setText("重新发送");
                view.setClickable(true);
            }
        };

        //在子线程中执行
        Flowable flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                int ss = second;//倒计时ss秒
                while (ss > 0 && !release) {
                    e.onNext(ss);
                    ss--;
                    Thread.sleep(1000);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);//BUFFER就是把RxJava中默认的只能存128个事件的缓存池换成一个大的缓存池，支持存很多很多的数据。
        flowable.subscribeOn(Schedulers.io()).doOnSubscribe(consumer)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    public static void release() {
        release = true;
    }

}
