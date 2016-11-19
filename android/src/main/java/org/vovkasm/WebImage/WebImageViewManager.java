package org.vovkasm.WebImage;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

class WebImageViewManager extends SimpleViewManager<ImageView> {
    private static final String REACT_CLASS = "WebImageView";

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
        Glide.with(view.getContext()).load(uri).into(view);
    }
}
