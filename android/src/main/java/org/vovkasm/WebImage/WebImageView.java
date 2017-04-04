package org.vovkasm.WebImage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.view.View;

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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class WebImageView extends View {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SCALE_CONTAIN, SCALE_COVER, SCALE_STRETCH, SCALE_CENTER})
    @interface ScaleType {}
    public static final int SCALE_CONTAIN = 0;
    public static final int SCALE_COVER = 1;
    public static final int SCALE_STRETCH = 2;
    public static final int SCALE_CENTER = 3;

    public static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
    public static final float DEFAULT_BORDER_RADIUS = 0f;

    private static RequestListener<Uri, GlideDrawable> requestListener = new WebImageViewRequestListener();

    private Uri mUri;
    private @ScaleType int mScaleType = SCALE_CONTAIN;

    private BoxMetrics mBoxMetrics;
    private @ColorInt int mBorderColor = DEFAULT_BORDER_COLOR;
    private @ColorInt int[] mBorderColors = new int[]{DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR};
    private float mBorderRadius = DEFAULT_BORDER_RADIUS;
    private float[] mBorderRadii = new float[]{YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED};

    private Drawable mImgDrawable;
    private int mImgDrawableWidth;
    private int mImgDrawableHeight;
    private Matrix mDrawMatrix = null;

    // Avoid allocations...
    private RectF mTempSrc = new RectF();
    private RectF mTempDst = new RectF();

    private IBorder mBorder;

    public WebImageView(Context context) {
        super(context);
        mBoxMetrics = new BoxMetrics();
        configureBounds();
    }

    public void setImageDrawable(Drawable drawable) {
        if (mImgDrawable != drawable) {
            final int oldWidth = mImgDrawableWidth;
            final int oldHeight = mImgDrawableHeight;

            updateDrawable(drawable);

            if (oldWidth != mImgDrawableWidth || oldHeight != mImgDrawableHeight) {
                requestLayout();
            }
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            configureBounds();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void updateDrawable(Drawable d) {
        if (mImgDrawable != null) {
            mImgDrawable.setCallback(null);
            unscheduleDrawable(mImgDrawable);
        }

        mImgDrawable = d;

        if (d != null) {
            d.setCallback(this);
            d.setVisible(getVisibility() == VISIBLE, true);
            mImgDrawableWidth = d.getIntrinsicWidth();
            mImgDrawableHeight = d.getIntrinsicHeight();
            configureBounds();
        } else {
            mImgDrawableWidth = mImgDrawableHeight = -1;
        }
    }

    private void configureBounds() {
        if (mImgDrawable == null) return;

        float cBoxWidth = mBoxMetrics.getContentWidth();
        float cBoxHeight = mBoxMetrics.getContentHeight();

        int imWidth = mImgDrawableWidth;
        int imHeight = mImgDrawableHeight;

        boolean fits = (imWidth < 0 || cBoxWidth == imWidth) && (imHeight < 0 || cBoxHeight == imHeight);

        if (imWidth <= 0 || imHeight <= 0 || SCALE_STRETCH == mScaleType) {
            mImgDrawable.setBounds(0, 0, Math.round(cBoxWidth), Math.round(cBoxHeight));
            mDrawMatrix = null;
        } else {
            mImgDrawable.setBounds(0, 0, imWidth, imHeight);

            if (fits) {
                mDrawMatrix = null;
            } else if (SCALE_CENTER == mScaleType) {
                mDrawMatrix = new Matrix();
                mDrawMatrix.setTranslate(Math.round((cBoxWidth - imWidth) * 0.5f), Math.round((cBoxHeight - imHeight) * 0.5f));
            } else if (SCALE_COVER == mScaleType) {
                mDrawMatrix = new Matrix();

                float scale;
                float dx = 0, dy = 0;

                if (imWidth * cBoxHeight > cBoxWidth * imHeight) {
                    scale = cBoxHeight / (float) imHeight;
                    dx = (cBoxWidth - imWidth * scale) * 0.5f;
                } else {
                    scale = cBoxWidth / (float) imWidth;
                    dy = (cBoxHeight - imHeight * scale) * 0.5f;
                }

                mDrawMatrix.setScale(scale, scale);
                mDrawMatrix.postTranslate(Math.round(dx), Math.round(dy));
            } else {
                // contain
                mTempSrc.set(0, 0, imWidth, imHeight);
                mTempDst.set(0, 0, cBoxWidth, cBoxHeight);

                mDrawMatrix = new Matrix();
                mDrawMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
            }
        }

        if (mDrawMatrix != null) {
            mTempSrc.set(0, 0, imWidth, imHeight);
            mDrawMatrix.mapRect(mTempDst, mTempSrc);
            mBoxMetrics.ajustContentSize(mTempDst.width(), mTempDst.height());
        }

        final float tl = YogaConstants.isUndefined(mBorderRadii[0]) ? mBorderRadius : mBorderRadii[0];
        final float tr = YogaConstants.isUndefined(mBorderRadii[1]) ? mBorderRadius : mBorderRadii[1];
        final float br = YogaConstants.isUndefined(mBorderRadii[2]) ? mBorderRadius : mBorderRadii[2];
        final float bl = YogaConstants.isUndefined(mBorderRadii[3]) ? mBorderRadius : mBorderRadii[3];
        mBoxMetrics.setRadii(tl, tr, br, bl);

        if (hasBorder()) {
            if (hasMonoBorder()) {
                MonoBorder monoBorder = null;
                if (mBorder instanceof MonoBorder)
                    monoBorder = (MonoBorder)mBorder;
                if (monoBorder == null) {
                    monoBorder = new MonoBorder();
                }

                monoBorder.setColor(mBorderColors[0] == Color.TRANSPARENT ? mBorderColor : mBorderColors[0]);

                mBorder = monoBorder;
            } else {
                MulticolorBorder multicolorBorder = null;
                if (mBorder instanceof MulticolorBorder)
                    multicolorBorder = (MulticolorBorder) mBorder;
                if (multicolorBorder == null) {
                    multicolorBorder = new MulticolorBorder();
                }

                final int lc = mBorderColors[0] == Color.TRANSPARENT ? mBorderColor : mBorderColors[0];
                final int tc = mBorderColors[1] == Color.TRANSPARENT ? mBorderColor : mBorderColors[1];
                final int rc = mBorderColors[2] == Color.TRANSPARENT ? mBorderColor : mBorderColors[2];
                final int bc = mBorderColors[3] == Color.TRANSPARENT ? mBorderColor : mBorderColors[3];
                multicolorBorder.setColors(lc, tc, rc, bc);

                mBorder = multicolorBorder;
            }

            mBorder.setMetrics(mBoxMetrics);
        } else {
            // no borders
            mBorder = null;
        }
    }

    public void setScaleType(@ScaleType int scaleType) {
        if (mScaleType == scaleType) {
            return;
        }

        mScaleType = scaleType;

        setWillNotCacheDrawing(mScaleType == SCALE_CENTER);

        requestLayout();
        invalidate();
    }

    public @ScaleType int getScaleType() {
        return mScaleType;
    }

    void setImageUri(Uri uri) {
        if (uri.equals(mUri)) return;
        mUri = uri;
        // TODO(vovkasm): use ThemedReactContext#getCurrentActivity so glide can follow lifecycle
        Glide.with(getContext()).load(mUri).listener(requestListener).into(new WebImageViewTarget(this));
    }

    public void setBorderColor(@ColorInt int color) {
        if (mBorderColor == color) return;
        mBorderColor = color;
        invalidate();
    }

    public void setBorderColor(@ColorInt int color, int side) {
        if (mBorderColors[side] == color) return;
        mBorderColors[side] = color;
        invalidate();
    }

    public void setBorderRadius(float radius) {
        if (FloatUtil.floatsEqual(mBorderRadius,radius)) return;
        mBorderRadius = radius;
        invalidate();
    }

    public void setBorderRadius(float radius, int index) {
        if (FloatUtil.floatsEqual(mBorderRadii[index], radius)) return;
        mBorderRadii[index] = radius;
        invalidate();
    }

    public void setBoxMetrics(ShadowBoxMetrics shadowMetrics) {
        mBoxMetrics.setShadowMetrics(shadowMetrics);
        configureBounds();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mImgDrawable == null) return;
        if (mImgDrawableWidth == 0 || mImgDrawableHeight == 0) return;

        if (mDrawMatrix == null) {
            mImgDrawable.draw(canvas);
        } else {
            int saveCount = canvas.getSaveCount();
            canvas.save();

            if (mBorder != null) {
                mBorder.draw(canvas);
            }
            canvas.clipPath(mBoxMetrics.getContentPath());

            canvas.translate(mBoxMetrics.borderLeft + mBoxMetrics.paddingLeft, mBoxMetrics.borderTop + mBoxMetrics.paddingTop);
            canvas.concat(mDrawMatrix);

            mImgDrawable.draw(canvas);

            canvas.restoreToCount(saveCount);
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
