package org.vovkasm.WebImage;

import android.graphics.Color;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.facebook.react.bridge.ReadableMap;
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

import javax.annotation.Nullable;

@ReactModule(name = WebImageViewManager.REACT_CLASS)
class WebImageViewManager extends BaseViewManager<WebImageView, WebImageShadowNode> {
    static final String REACT_CLASS = "WebImageView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected WebImageView createViewInstance(ThemedReactContext reactContext) {
        return new WebImageView(reactContext);
    }

    @Override
    public void onDropViewInstance(WebImageView view) {
        super.onDropViewInstance(view);
        Glide.clear(view);
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
        final Uri uri = Uri.parse(uriProp);
        view.setImageUri(uri);
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
        return exportedEvents;
    }

}
