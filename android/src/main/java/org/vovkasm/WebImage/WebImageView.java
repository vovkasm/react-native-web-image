package org.vovkasm.WebImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import android.view.View;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.facebook.react.uimanager.FloatUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.yoga.YogaConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class WebImageView extends View {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SCALE_CONTAIN, SCALE_COVER, SCALE_STRETCH, SCALE_CENTER})
    @interface ScaleType {
    }

    public static final int SCALE_CONTAIN = 0;
    public static final int SCALE_COVER = 1;
    public static final int SCALE_STRETCH = 2;
    public static final int SCALE_CENTER = 3;

    public static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
    public static final float DEFAULT_BORDER_RADIUS = 0f;

    private GlideUrl mUri;
    private @ScaleType
    int mScaleType = SCALE_CONTAIN;

    private BoxMetrics mBoxMetrics;
    private @ColorInt
    int mBorderColor = DEFAULT_BORDER_COLOR;
    private @ColorInt
    int[] mBorderColors = new int[]{DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR};
    private float mBorderRadius = DEFAULT_BORDER_RADIUS;
    private float[] mBorderRadii = new float[]{YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED};

    private Bitmap mBitmap = null;
    private Paint mBitmapPaint = new Paint();
    private BitmapShader mBitmapShader = null;

    private IBorder mBorder;

    private WebImageViewTarget mGlideTarget;
    private RequestListener mGlideListener;
    private RequestManager mRequestManager;

    public WebImageView(Context context, RequestListener glideListener, RequestManager requestManager) {
        super(context);
        mBoxMetrics = new BoxMetrics(mScaleType);
        mBitmapPaint.setAntiAlias(true);
        mGlideTarget = new WebImageViewTarget(this);
        mGlideListener = glideListener;
        mRequestManager = requestManager;
        configureBounds();
    }

    void setBitmap(@Nullable Bitmap bitmap) {
        mBitmap = bitmap;
        if (mBitmap != null) {
            mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setShader(mBitmapShader);
            mBoxMetrics.setImageSize(mBitmap.getWidth(), mBitmap.getHeight());
        } else {
            mBitmapShader = null;
            mBitmapPaint.setShader(null);
            mBoxMetrics.setImageSize(0, 0);
        }
        configureBounds();
        requestLayout();
        invalidate();
    }

    ThemedReactContext getThemedReactContext() {
        return (ThemedReactContext) getContext();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            mBoxMetrics.setSize(right - left, bottom - top);
            configureBounds();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void configureBounds() {
        if (mBitmap == null) return;

        final float tl = YogaConstants.isUndefined(mBorderRadii[0]) ? mBorderRadius : mBorderRadii[0];
        final float tr = YogaConstants.isUndefined(mBorderRadii[1]) ? mBorderRadius : mBorderRadii[1];
        final float br = YogaConstants.isUndefined(mBorderRadii[2]) ? mBorderRadius : mBorderRadii[2];
        final float bl = YogaConstants.isUndefined(mBorderRadii[3]) ? mBorderRadius : mBorderRadii[3];
        mBoxMetrics.setRadii(tl, tr, br, bl);

        mBitmapShader.setLocalMatrix(mBoxMetrics.getContentMatrix());
        mBitmapPaint.setShader(mBitmapShader);

        if (hasBorder()) {
            if (hasMonoBorder()) {
                MonoBorder monoBorder = null;
                if (mBorder instanceof MonoBorder)
                    monoBorder = (MonoBorder) mBorder;
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
        if (mScaleType == scaleType) return;

        mScaleType = scaleType;
        mBoxMetrics.setScaleType(scaleType);

        invalidate();
    }

    public @ScaleType
    int getScaleType() {
        return mScaleType;
    }

    void setImageUri(GlideUrl uri) {
        if (uri.equals(mUri)) return;
        mUri = uri;
        mRequestManager.asBitmap().load(mUri).listener(mGlideListener).into(mGlideTarget);
    }

    final GlideUrl getImageUri() {
        return mUri;
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
        if (FloatUtil.floatsEqual(mBorderRadius, radius)) return;
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
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap == null) return;

        if (mBorder != null) {
            mBorder.draw(canvas);
        }

        canvas.drawPath(mBoxMetrics.getContentPath(), mBitmapPaint);
    }

    private boolean hasBorder() {
        return mBoxMetrics != null && mBoxMetrics.hasBorder();
    }

    private boolean hasMonoBorder() {
        return (mBorderColors[0] == mBorderColors[1]
                && mBorderColors[1] == mBorderColors[2]
                && mBorderColors[2] == mBorderColors[3]);
    }

    public void clear() {
        mRequestManager.clear(this);
    }

}
