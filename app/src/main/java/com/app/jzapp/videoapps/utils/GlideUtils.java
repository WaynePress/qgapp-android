package com.app.jzapp.videoapps.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.app.jzapp.videoapps.http.AppConfig;
import com.app.video.videoapps.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class GlideUtils {

    public static RequestOptions optionsCircle = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .centerCrop()
            .error(R.mipmap.ic_default)
            .placeholder(R.mipmap.ic_default)
            .transform(new CircleTransform());

    public static RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .centerCrop()
            .error(R.mipmap.ic_default)
            .placeholder(R.mipmap.ic_default);

    public static RequestOptions optionsMv = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(R.mipmap.ic_default)
            .placeholder(R.mipmap.ic_default)
            .transform(new MVTransform());


    private static RequestOptions requestOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .centerCrop()
            .error(R.mipmap.ic_default)
            .placeholder(R.mipmap.ic_default)
            .transform(new GlideRoundTransform(5));

    public static void loadCircleImagView(Context context, String url, ImageView imageView) {
        if (url != null && url.contains("http")) {
            Glide.with(context).load(url).apply(optionsCircle).into(imageView);
        } else {
            Glide.with(context).load(AppConfig.getUrl() + url).apply(optionsCircle).into(imageView);
        }
    }

    public static void loadCircleImagView(Context context, int id, ImageView imageView) {
        Glide.with(context).load(id).apply(optionsCircle).into(imageView);
    }

    public static void loadImagView(Context context, String url, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (url != null && url.contains("http"))
            Glide.with(context).load(url).apply(options).into(imageView);
        else
            Glide.with(context).load(AppConfig.getUrl() + url).apply(options).into(imageView);
    }

    public static void loadMVImagView(final Context context, String url,final ImageView imageView) {
        imageView.setBackground(null);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (url == null || !url.contains("http")){
            url = AppConfig.getUrl() + url;
        }

        Glide.with(context).load(url).apply(optionsMv).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource.getIntrinsicHeight()>=resource.getIntrinsicWidth()){
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setBackgroundColor(context.getResources().getColor(R.color.black));
                }
                return false;
            }
        }).into(imageView);

    }

    public static void loadImagView2(Context context, String url, ImageView imageView) {
        if (url != null && url.contains("http"))
            Glide.with(context).load(url).apply(options).into(imageView);
        else
            Glide.with(context).load(AppConfig.getUrl() + url).apply(requestOptions).into(imageView);
    }


}
