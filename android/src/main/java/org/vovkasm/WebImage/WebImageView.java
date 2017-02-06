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

    private int mBorderColor = DEFAULT_BORDER_COLOR;
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
    public void setBackgroundColor(@ColorInt int color) {
        if (mDrawable instanceof RoundedDrawable) {
            ((RoundedDrawable) mDrawable).setColor(color);
        }
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
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
        mBorderColor = color;
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

            for (int side=0; side<4; side++) {
                if (YogaConstants.isUndefined(mBorderSizes[side])) {
                    roundedDrawable.setBorderWidth(side, mBorderWidth);
                } else {
                    roundedDrawable.setBorderWidth(side, mBorderSizes[side]);
                }
            }

            roundedDrawable.setScaleType(getScaleType());
            roundedDrawable.setBorderColor(mBorderColor);

            for (RoundedDrawable.Corner corner : RoundedDrawable.Corner.values()) {
                if (YogaConstants.isUndefined(mBorderRadii[corner.index])) {
                    roundedDrawable.setCornerRadius(corner, mBorderRadius);
                } else {
                    roundedDrawable.setCornerRadius(corner, mBorderRadii[corner.index]);
                }
            }

        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) drawable;
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

}
