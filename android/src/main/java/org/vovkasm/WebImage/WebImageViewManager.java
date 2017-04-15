package org.vovkasm.WebImage;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

class WebImageViewManager extends SimpleViewManager<ImageView> {
    private static final String REACT_CLASS = "WebImageView";

    private static Map<String, ImageView.ScaleType> RESIZE_MODE_MAP = new HashMap<String, ImageView.ScaleType>(){{
        put("cover", ScaleType.CENTER_CROP);
        put("contain", ScaleType.FIT_CENTER);
        put("stretch", ScaleType.FIT_XY);
        put("center", ScaleType.CENTER);
    }};

    private static RequestListener LISTENER = new RequestListener<Uri,GlideDrawable>() {
        @Override
        public boolean onException(Exception e, Uri uri, Target<GlideDrawable> target, boolean isFirstResource) {
            if (!(target instanceof ImageViewTarget)) {
                return false;
            }
            ImageView view = (ImageView) ((ImageViewTarget) target).getView();
            WritableMap event = Arguments.createMap();
            if (e != null) {
                event.putString("error", e.getMessage());
            } else {
                event.putString("error", "Unknown");
            }
            event.putString("uri", uri.toString());
            ThemedReactContext context = (ThemedReactContext) view.getContext();
            context.getJSModule(RCTEventEmitter.class).receiveEvent(view.getId(), "onWebImageError", event);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, Uri uri, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

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
        final ThemedReactContext context = (ThemedReactContext) view.getContext();
        if (context == null) return;

        // Guard against destroyed activity (see: https://github.com/bumptech/glide/issues/803)
        final Activity activity = context.getCurrentActivity();
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) return;

        final String uriProp = source.getString("uri");
        final Uri uri = Uri.parse(uriProp);
        Glide.with(activity).load(uri).listener(LISTENER).into(view);
    }

    @ReactProp(name="resizeMode")
    public void setResizeMode(ImageView view, String resizeMode) {
        ImageView.ScaleType scaleType = RESIZE_MODE_MAP.get(resizeMode);

        if (scaleType != null) {
            view.setScaleType(scaleType);
        }
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedEvents = new HashMap<>();
        Map<String, String> onErrorEventExport = new HashMap<>();
        onErrorEventExport.put("registrationName", "onWebImageError");
        exportedEvents.put("onWebImageError", onErrorEventExport);
        return exportedEvents;
    }
}
