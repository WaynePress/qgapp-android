package com.app.jzapp.videoapps.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class MVTransform extends BitmapTransformation {

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform, outWidth, outHeight);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Log.e("cjn","MVTransform   ");
        if (toTransform == null) return null;
        Log.e("cjn","getHeight   "+toTransform.getHeight() +" getWidth "+ toTransform.getWidth());
        if (toTransform.getHeight() >= toTransform.getWidth()) {
            return TransformationUtils.fitCenter(pool, toTransform, outWidth, outHeight);
        }

        return TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
