package org.vovkasm.WebImage;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.HashMap;
import java.util.Map;

class WebImageViewManager extends SimpleViewManager<ImageView> {
    private static final String REACT_CLASS = "WebImageView";

    private static Map<String, ImageView.ScaleType> RESIZE_MODE_MAP = new HashMap<String, ImageView.ScaleType>(){{
        put("cover", ScaleType.CENTER_CROP);
        put("contain", ScaleType.FIT_CENTER);
        put("stretch", ScaleType.FIT_XY);
        put("center", ScaleType.CENTER);
    }};

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

    @ReactProp(name="resizeMode")
    public void setResizeMode(ImageView view, String resizeMode) {
        ImageView.ScaleType scaleType = RESIZE_MODE_MAP.get(resizeMode);

        if (scaleType != null) {
            view.setScaleType(scaleType);
        }
    }

}
