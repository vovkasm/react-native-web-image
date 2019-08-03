package org.vovkasm.WebImage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.facebook.react.bridge.NoSuchKeyException;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.yoga.YogaConstants;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = WebImageViewManager.REACT_CLASS)
class WebImageViewManager extends BaseViewManager<WebImageView, WebImageShadowNode> {
    static final String REACT_CLASS = "WebImageView";

    RequestListener mRequestListener = new RequestListener();

    @Override
    public @NonNull
    String getName() {
        return REACT_CLASS;
    }

    @Override
    protected @NonNull
    WebImageView createViewInstance(@NonNull ThemedReactContext reactContext) {
        RequestManager requestManager;
        Activity activity = getActivityForGlide(reactContext);
        if (activity != null) {
            requestManager = Glide.with(activity);
        } else {
            requestManager = Glide.with(reactContext);
        }
        return new WebImageView(reactContext, mRequestListener, requestManager);
    }

    @Override
    public void onDropViewInstance(@NonNull WebImageView view) {
        super.onDropViewInstance(view);
        view.clear();
    }

    @Override
    public WebImageShadowNode createShadowNodeInstance() {
        return new WebImageShadowNode();
    }

    @Override
    public Class<? extends WebImageShadowNode> getShadowNodeClass() {
        return WebImageShadowNode.class;
    }

    @ReactProp(name = "source")
    public void setSrc(WebImageView view, @Nullable ReadableMap source) {
        if (source == null) return;
        final String uriProp = source.getString("uri");

        // Get the headers prop and add to glideUrl
        GlideUrl glideUrl;
        try {
            final ReadableMap headersMap = source.getMap("headers");
            ReadableMapKeySetIterator headersIterator = headersMap.keySetIterator();
            LazyHeaders.Builder headersBuilder = new LazyHeaders.Builder();
            while (headersIterator.hasNextKey()) {
                String key = headersIterator.nextKey();
                String value = headersMap.getString(key);
                if (value != null) headersBuilder.addHeader(key, value);
            }
            LazyHeaders headers = headersBuilder.build();
            glideUrl = new GlideUrl(uriProp, headers);
        } catch (NoSuchKeyException e) {
            // If there is no headers object, return just the uri
            glideUrl = new GlideUrl(uriProp);
        }

        view.setImageUri(glideUrl);
    }

    @ReactProp(name = "resizeMode")
    public void setResizeMode(WebImageView view, String resizeMode) {
        @WebImageView.ScaleType int scaleType;

        switch (resizeMode) {
            case "contain":
                view.setScaleType(WebImageView.SCALE_CONTAIN);
                break;
            case "cover":
                view.setScaleType(WebImageView.SCALE_COVER);
                break;
            case "stretch":
                view.setScaleType(WebImageView.SCALE_STRETCH);
                break;
            case "center":
                view.setScaleType(WebImageView.SCALE_CENTER);
        }
    }

    @ReactPropGroup(names = {
            "borderColor",
            "borderLeftColor",
            "borderTopColor",
            "borderRightColor",
            "borderBottomColor"
    }, customType = "Color")
    public void setBorderColor(WebImageView view, int index, @Nullable Integer color) {
        if (color == null) {
            color = Color.TRANSPARENT;
        }
        if (index == 0) {
            view.setBorderColor(color);
        } else {
            view.setBorderColor(color, index - 1);
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

    @Override
    public void updateExtraData(@NonNull WebImageView view, Object extraData) {
        if (extraData instanceof ShadowBoxMetrics) {
            ShadowBoxMetrics bm = (ShadowBoxMetrics) extraData;
            view.setBoxMetrics(bm);
        }
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedEvents = new HashMap<>();
        Map<String, String> onErrorEventExport = new HashMap<>();
        onErrorEventExport.put("registrationName", "onWebImageError");
        exportedEvents.put("onWebImageError", onErrorEventExport);

        // declare success event
        Map<String, String> onLoadEventExport = new HashMap<>();
        onLoadEventExport.put("registrationName", "onWebImageLoad");
        exportedEvents.put("onWebImageLoad", onLoadEventExport);

        return exportedEvents;
    }

    private Activity getActivityForGlide(@NonNull ThemedReactContext ctx) {
        // Guard against destroyed activity (see: https://github.com/bumptech/glide/issues/803)
        final Activity activity = ctx.getCurrentActivity();
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())
                return null;
        }
        return activity;
    }

}
