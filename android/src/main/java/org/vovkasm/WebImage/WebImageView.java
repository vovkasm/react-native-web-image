package org.vovkasm.WebImage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.FloatUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.yoga.YogaConstants;

class WebImageView extends ImageView {
    public static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
    public static final float DEFAULT_BORDER_RADIUS = 0f;

    private static RequestListener requestListener = new WebImageViewRequestListener();

    private Uri mUri;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    private BoxMetrics mBoxMetrics;
    private @ColorInt int mBorderColor = DEFAULT_BORDER_COLOR;
    private @ColorInt int mBorderColors[] = new int[]{DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR};
    private float mBorderRadius = DEFAULT_BORDER_RADIUS;
    private float[] mBorderRadii = new float[]{YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED};
    private Drawable mDrawable;

    public WebImageView(Context context) {
        super(context);
        updateDrawableAttrs();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mDrawable = BackgroundDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.FIT_XY);
        if (mScaleType == scaleType) {
            return;
        }
        mScaleType = scaleType;
        updateDrawableAttrs();
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    void setImageUri(Uri uri) {
        if (uri.equals(mUri)) return;
        mUri = uri;
        // TODO(vovkasm): use ThemedReactContext#getCurrentActivity so glide can follow lifecycle
        Glide.with(getContext()).load(mUri).listener(requestListener).into(new WebImageViewTarget(this));
    }

    public void setBorderColor(@ColorInt int color) {
        if (mBorderColor == color) {
            return;
        }
        mBorderColor = color;
        updateDrawableAttrs();
        invalidate();
    }

    public void setBorderColor(@ColorInt int color, int side) {
        if (mBorderColors[side] == color) {
            return;
        }
        mBorderColors[side] = color;
        updateDrawableAttrs();
        invalidate();
    }

    public void setBorderRadius(float radius) {
        if (FloatUtil.floatsEqual(mBorderRadius,radius)) {
            return;
        }
        mBorderRadius = radius;
        updateDrawableAttrs();
        invalidate();
    }

    public void setBorderRadius(float radius, int index) {
        if (FloatUtil.floatsEqual(mBorderRadii[index], radius)) {
            return;
        }
        mBorderRadii[index] = radius;
        updateDrawableAttrs();
        invalidate();
    }

    public void setBoxMetrics(BoxMetrics bm) {
        if (mBoxMetrics != null && mBoxMetrics.equalsToBoxMetrics(bm)) {
            return;
        }
        mBoxMetrics = bm;
        updateDrawableAttrs();
        invalidate();
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    private void updateAttrs(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof BackgroundDrawable) {
            BackgroundDrawable backgroundDrawable = (BackgroundDrawable) drawable;

            if (hasBorder()) {
                IBorder border = backgroundDrawable.getBorder();

                if (hasMonoBorder()) {
                    MonoBorder monoBorder = null;
                    if (border instanceof MonoBorder)
                        monoBorder = (MonoBorder)border;
                    if (monoBorder == null) {
                        monoBorder = new MonoBorder();
                    }

                    if (mBoxMetrics == null) {
                        monoBorder.setWidths(0f, 0f, 0f, 0f);
                    } else {
                        monoBorder.setWidths(mBoxMetrics.getBorderLeft(), mBoxMetrics.getBorderTop(), mBoxMetrics.getBorderRight(), mBoxMetrics.getBorderBottom());
                    }

                    final float tl = YogaConstants.isUndefined(mBorderRadii[0]) ? mBorderRadius : mBorderRadii[0];
                    final float tr = YogaConstants.isUndefined(mBorderRadii[1]) ? mBorderRadius : mBorderRadii[1];
                    final float br = YogaConstants.isUndefined(mBorderRadii[2]) ? mBorderRadius : mBorderRadii[2];
                    final float bl = YogaConstants.isUndefined(mBorderRadii[3]) ? mBorderRadius : mBorderRadii[3];
                    monoBorder.setRadii(tl, tr, br, bl);

                    monoBorder.setColor(mBorderColors[0] == Color.TRANSPARENT ? mBorderColor : mBorderColors[0]);

                    backgroundDrawable.setBorder(monoBorder);
                } else {
                    MulticolorBorder multicolorBorder = null;
                    if (border instanceof MulticolorBorder)
                        multicolorBorder = (MulticolorBorder) border;
                    if (multicolorBorder == null) {
                        multicolorBorder = new MulticolorBorder();
                    }

                    if (mBoxMetrics == null) {
                        multicolorBorder.setWidths(0f, 0f, 0f, 0f);
                    } else {
                        multicolorBorder.setWidths(mBoxMetrics.getBorderLeft(), mBoxMetrics.getBorderTop(), mBoxMetrics.getBorderRight(), mBoxMetrics.getBorderBottom());
                    }

                    final int lc = mBorderColors[0] == Color.TRANSPARENT ? mBorderColor : mBorderColors[0];
                    final int tc = mBorderColors[1] == Color.TRANSPARENT ? mBorderColor : mBorderColors[1];
                    final int rc = mBorderColors[2] == Color.TRANSPARENT ? mBorderColor : mBorderColors[2];
                    final int bc = mBorderColors[3] == Color.TRANSPARENT ? mBorderColor : mBorderColors[3];
                    multicolorBorder.setColors(lc, tc, rc, bc);

                    final float tl = YogaConstants.isUndefined(mBorderRadii[0]) ? mBorderRadius : mBorderRadii[0];
                    final float tr = YogaConstants.isUndefined(mBorderRadii[1]) ? mBorderRadius : mBorderRadii[1];
                    final float br = YogaConstants.isUndefined(mBorderRadii[2]) ? mBorderRadius : mBorderRadii[2];
                    final float bl = YogaConstants.isUndefined(mBorderRadii[3]) ? mBorderRadius : mBorderRadii[3];
                    multicolorBorder.setRadii(tl, tr, br, bl);

                    backgroundDrawable.setBorder(multicolorBorder);
                }
            } else {
                // no borders
                if (backgroundDrawable.getBorder() != null) backgroundDrawable.setBorder(null);
            }
            backgroundDrawable.setScaleType(getScaleType());
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) drawable;
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

    private boolean hasBorder() {
        return mBoxMetrics != null && mBoxMetrics.hasBorder();
    }

    private boolean hasMonoBorder() {
        return (mBorderColors[0] == mBorderColors[1]
                && mBorderColors[1] == mBorderColors[2]
                && mBorderColors[2] == mBorderColors[3]);
    }

    private static class WebImageViewRequestListener implements RequestListener<Uri,GlideDrawable> {
        @Override
        public boolean onException(Exception e, Uri uri, Target<GlideDrawable> target, boolean isFirstResource) {
            if (!(target instanceof WebImageViewTarget)) return false;
            WebImageView view = ((WebImageViewTarget) target).getView();
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
    }

    private static class WebImageViewTarget extends ViewTarget<WebImageView, GlideDrawable> {
        WebImageViewTarget(WebImageView view) {
            super(view);
        }

        @Override
        public void onResourceReady(GlideDrawable drawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
            this.view.setImageDrawable(drawable);
        }

        public WebImageView getView() { return view; }
    }
}
