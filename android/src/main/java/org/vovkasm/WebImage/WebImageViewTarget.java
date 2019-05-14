package org.vovkasm.WebImage;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class WebImageViewTarget extends CustomViewTarget<WebImageView, Bitmap> {
    WebImageViewTarget(WebImageView view) {
        super(view);
    }

    @Override
    protected void onResourceCleared(@Nullable Drawable placeholder) {
        view.setBitmap(null);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        view.setBitmap(null);
    }

    @Override
    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
        view.setBitmap(bitmap);
    }
}
