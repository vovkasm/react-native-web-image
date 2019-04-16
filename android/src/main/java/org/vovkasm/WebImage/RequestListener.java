package org.vovkasm.WebImage;

import android.graphics.Bitmap;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.Target;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;

class RequestListener implements com.bumptech.glide.request.RequestListener {

    @Override
    public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        WebImageView view = null;
        if (target instanceof WebImageViewTarget) {
            view = ((WebImageViewTarget) target).getView();
        }
        if (view != null) {
            ThemedReactContext context = view.getThemedReactContext();
            if (context != null) {
                WritableMap event = createMap();
                if (e != null) {
                    event.putString("error", e.getMessage());
                } else {
                    event.putString("error", "Unknown");
                }
                final GlideUrl uri = view.getImageUri();
                if (uri != null) {
                    event.putString("uri", uri.toStringUrl());
                }
                context.getJSModule(RCTEventEmitter.class).receiveEvent(view.getId(), "onWebImageError", event);
            }
        }
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        WebImageView view = null;
        if (target instanceof WebImageViewTarget) {
            view = ((WebImageViewTarget) target).getView();
        }
        Bitmap bitmap = null;
        if (resource instanceof Bitmap) {
            bitmap = (Bitmap) resource;
        }
        if (view != null) {
            ThemedReactContext context = view.getThemedReactContext();
            if (context != null) {
                WritableMap event = createMap();
                WritableMap source = createMap();
                final GlideUrl uri = view.getImageUri();
                if (uri != null) {
                    source.putString("uri", uri.toStringUrl());
                }
                if (bitmap != null) {
                    source.putInt("width", bitmap.getWidth());
                    source.putInt("height", bitmap.getHeight());
                }
                event.putMap("source", source);
                context.getJSModule(RCTEventEmitter.class).receiveEvent(view.getId(), "onWebImageLoad", event);
            }
        }

        return false;
    }

    WritableMap createMap() {
        return Arguments.createMap();
    }
}
