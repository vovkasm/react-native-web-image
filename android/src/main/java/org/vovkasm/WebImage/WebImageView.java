package org.vovkasm.WebImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.widget.ImageView;

import com.facebook.react.uimanager.FloatUtil;
import com.facebook.yoga.YogaConstants;

public class WebImageView extends ImageView {
    public static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;
    public static final float DEFAULT_BORDER_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;

    private @ColorInt int mBorderColor = DEFAULT_BORDER_COLOR;
    private @ColorInt int mBorderColors[] = new int[]{DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR};
    private float mBorderRadius = DEFAULT_BORDER_RADIUS;
    private float[] mBorderRadii = new float[]{YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED};
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;
    private float[] mBorderSizes = new float[]{YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED, YogaConstants.UNDEFINED};
    private Drawable mDrawable;

    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    public WebImageView(Context context) {
        super(context);
        updateDrawableAttrs();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mDrawable = RoundedDrawable.fromBitmap(bm);
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

    public void setBorderWidth(float width) {
        if (mBorderWidth == width) {
            return;
        }
        mBorderWidth = width;
        updateDrawableAttrs();
        invalidate();
    }

    public void setBorderWidth(float width, int index) {
        if (FloatUtil.floatsEqual(mBorderSizes[index], width)) {
            return;
        }
        mBorderSizes[index] = width;
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

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    private void updateAttrs(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof RoundedDrawable) {
            RoundedDrawable roundedDrawable = (RoundedDrawable) drawable;

            if (hasBorder()) {
                Border border = roundedDrawable.getBorder();
                if (border == null) {
                    border = new Border();
                }

                final float lw = YogaConstants.isUndefined(mBorderSizes[0]) ? mBorderWidth : mBorderSizes[0];
                final float tw = YogaConstants.isUndefined(mBorderSizes[1]) ? mBorderWidth : mBorderSizes[1];
                final float rw = YogaConstants.isUndefined(mBorderSizes[2]) ? mBorderWidth : mBorderSizes[2];
                final float bw = YogaConstants.isUndefined(mBorderSizes[3]) ? mBorderWidth : mBorderSizes[3];
                border.setWidths(lw, tw, rw, bw);

                final int lc = mBorderColors[0] == Color.TRANSPARENT ? mBorderColor : mBorderColors[0];
                final int tc = mBorderColors[1] == Color.TRANSPARENT ? mBorderColor : mBorderColors[1];
                final int rc = mBorderColors[2] == Color.TRANSPARENT ? mBorderColor : mBorderColors[2];
                final int bc = mBorderColors[3] == Color.TRANSPARENT ? mBorderColor : mBorderColors[3];
                border.setColors(lc, tc, rc, bc);

                final float tl = YogaConstants.isUndefined(mBorderRadii[0]) ? mBorderRadius : mBorderRadii[0];
                final float tr = YogaConstants.isUndefined(mBorderRadii[1]) ? mBorderRadius : mBorderRadii[1];
                final float br = YogaConstants.isUndefined(mBorderRadii[2]) ? mBorderRadius : mBorderRadii[2];
                final float bl = YogaConstants.isUndefined(mBorderRadii[3]) ? mBorderRadius : mBorderRadii[3];
                border.setRadii(tl, tr, br, bl);

                roundedDrawable.setBorder(border);
            } else {
                // no borders
                if (roundedDrawable.getBorder() != null) roundedDrawable.setBorder(null);
            }
            roundedDrawable.setScaleType(getScaleType());
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) drawable;
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

    private boolean hasBorder() {
        return !(YogaConstants.isUndefined(mBorderWidth)
                && YogaConstants.isUndefined(mBorderSizes[0])
                && YogaConstants.isUndefined(mBorderSizes[1])
                && YogaConstants.isUndefined(mBorderSizes[2])
                && YogaConstants.isUndefined(mBorderSizes[3]));
    }

}
