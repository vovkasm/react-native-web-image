package org.vovkasm.WebImage;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.Target;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NoSuchKeyException;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.yoga.YogaConstants;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

@ReactModule(name = WebImageViewManager.REACT_CLASS)
class WebImageViewManager extends BaseViewManager<WebImageView, WebImageShadowNode> {
    static final String REACT_CLASS = "WebImageView";

    RequestListener mRequestListener = new RequestListener();

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected WebImageView createViewInstance(ThemedReactContext reactContext) {
        return new WebImageView(reactContext, mRequestListener);
    }

    @Override
    public void onDropViewInstance(WebImageView view) {
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

    @ReactProp(name="source")
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
                headersBuilder.addHeader(key, value);
            }
            LazyHeaders headers = headersBuilder.build();
            glideUrl = new GlideUrl(uriProp, headers);
        } catch (NoSuchKeyException e) {
            // If there is no headers object, return just the uri
            glideUrl = new GlideUrl(uriProp);
        }

        view.setImageUri(glideUrl);
    }

    @ReactProp(name="resizeMode")
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
    public void updateExtraData(WebImageView view, Object extraData) {
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

    private static class RequestListener implements com.bumptech.glide.request.RequestListener {

        @Override
        public boolean onLoadFailed(@android.support.annotation.Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            WebImageView view = null;
            if (target instanceof WebImageView.WebImageViewTarget) {
                view = ((WebImageView.WebImageViewTarget)target).getView();
            }
            if (view != null) {
                ThemedReactContext context = view.getThemedReactContext();
                if (context != null) {
                    WritableMap event = Arguments.createMap();
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
            if (target instanceof WebImageView.WebImageViewTarget) {
                view = ((WebImageView.WebImageViewTarget)target).getView();
            }
            Bitmap bitmap = null;
            if (resource instanceof Bitmap) {
                bitmap = (Bitmap)resource;
            }
            if (view != null) {
                ThemedReactContext context = view.getThemedReactContext();
                if (context != null) {
                    WritableMap event = Arguments.createMap();
                    WritableMap source = Arguments.createMap();
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
    }
}
