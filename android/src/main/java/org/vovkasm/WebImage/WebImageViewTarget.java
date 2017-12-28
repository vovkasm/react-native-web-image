package org.vovkasm.WebImage;

import android.graphics.Bitmap;

import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class WebImageViewTarget extends ViewTarget<WebImageView, Bitmap> {
    WebImageViewTarget(WebImageView view) {
        super(view);
    }

    @Override
    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
        view.setBitmap(bitmap);
    }

    public WebImageView getView() {
        return view;
    }
}
