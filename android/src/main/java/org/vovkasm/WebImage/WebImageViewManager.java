package org.vovkasm.WebImage;

import android.graphics.Color;
import android.net.Uri;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.yoga.YogaConstants;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

class WebImageViewManager extends SimpleViewManager<WebImageView> {
    private static final String REACT_CLASS = "WebImageView";

    private static Map<String, ScaleType> RESIZE_MODE_MAP = new HashMap<String, ScaleType>(){{
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
            WebImageView view = (WebImageView) ((ImageViewTarget) target).getView();
            WritableMap event = Arguments.createMap();
            event.putString("error", e.getMessage());
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
    protected WebImageView createViewInstance(ThemedReactContext reactContext) {
        return new WebImageView(reactContext);
    }

    @ReactProp(name="source")
    public void setSrc(WebImageView view, @Nullable ReadableMap source) {
        if (source == null) return;
        final String uriProp = source.getString("uri");
        final Uri uri = Uri.parse(uriProp);
        Glide.with(view.getContext())
                .load(uri)
                .listener(LISTENER)
                .dontTransform()
                .into(view);
    }

    @ReactProp(name="resizeMode")
    public void setResizeMode(WebImageView view, String resizeMode) {
        ScaleType scaleType = RESIZE_MODE_MAP.get(resizeMode);

        if (scaleType != null) {
            view.setScaleType(scaleType);
        }
    }

    @ReactProp(name = "borderColor", customType = "Color")
    public void setBorderColor(WebImageView view, @Nullable Integer borderColor) {
        if (borderColor == null) {
            view.setBorderColor(Color.TRANSPARENT);
        } else {
            view.setBorderColor(borderColor);
        }
    }

    @ReactPropGroup(names = {
            ViewProps.BORDER_WIDTH,
            ViewProps.BORDER_LEFT_WIDTH,
            ViewProps.BORDER_TOP_WIDTH,
            ViewProps.BORDER_RIGHT_WIDTH,
            ViewProps.BORDER_BOTTOM_WIDTH
    }, defaultFloat = YogaConstants.UNDEFINED)
    public void setBorderWidth(WebImageView view, int index, float borderWidth) {
        if (!YogaConstants.isUndefined(borderWidth)) {
            borderWidth = PixelUtil.toPixelFromDIP(borderWidth);
        }

        if (index == 0) {
            view.setBorderWidth(borderWidth);
        } else {
            view.setBorderWidth(borderWidth, index - 1);
        }
    }

    @ReactPropGroup(names = {
            ViewProps.BORDER_RADIUS,
            ViewProps.BORDER_TOP_LEFT_RADIUS,
            ViewProps.BORDER_TOP_RIGHT_RADIUS,
            ViewProps.BORDER_BOTTOM_RIGHT_RADIUS,
            ViewProps.BORDER_BOTTOM_LEFT_RADIUS
    }, defaultFloat = YogaConstants.UNDEFINED)
    public void setBorderRadius(WebImageView view, int index, float borderRadius) {
        if (!YogaConstants.isUndefined(borderRadius)) {
            borderRadius = PixelUtil.toPixelFromDIP(borderRadius);
        }

        if (index == 0) {
            view.setBorderRadius(borderRadius);
        } else {
            view.setBorderRadius(borderRadius, index - 1);
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
