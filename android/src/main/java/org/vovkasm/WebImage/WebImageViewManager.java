package org.vovkasm.WebImage;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

class WebImageViewManager extends SimpleViewManager<ImageView> {
    private static final String REACT_CLASS = "WebImageView";

    private static class ResizeMode {
        static final String cover = "cover";
        static final String contain = "contain";
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ImageView createViewInstance(ThemedReactContext reactContext) {
        return new ImageView(reactContext);
    }

    @ReactProp(name="source")
    public void setSrc(ImageView view, @Nullable ReadableMap source) {
        if (source == null) return;
        final String uriProp = source.getString("uri");
        final Uri uri = Uri.parse(uriProp);
        final String resizeModeProp = source.hasKey("resizeMode") ? source.getString("resizeMode") : ResizeMode.contain;

        final DrawableTypeRequest request = Glide.with(view.getContext()).load(uri);
        DrawableRequestBuilder builder = null;

        if (ResizeMode.cover.equals(resizeModeProp)) {
            builder = request.centerCrop();
        } else if (ResizeMode.contain.equals(resizeModeProp)) {
            builder = request.fitCenter();
        }

        if (builder != null) {
            builder.into(view);
        } else {
            request.into(view);
        }
    }

}
