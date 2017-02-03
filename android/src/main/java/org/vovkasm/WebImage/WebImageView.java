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

            roundedDrawable.setBorderColor(mBorderColor);

            if (YogaConstants.isUndefined(mBorderRadii[0])) {
                roundedDrawable.setCornerRadius(Corner.TOP_LEFT, mBorderRadius);
            } else {
                roundedDrawable.setCornerRadius(Corner.TOP_LEFT, mBorderRadii[0]);
            }
            if (YogaConstants.isUndefined(mBorderRadii[1])) {
                roundedDrawable.setCornerRadius(Corner.TOP_RIGHT, mBorderRadius);
            } else {
                roundedDrawable.setCornerRadius(Corner.TOP_RIGHT, mBorderRadii[1]);
            }
            if (YogaConstants.isUndefined(mBorderRadii[2])) {
                roundedDrawable.setCornerRadius(Corner.BOTTOM_RIGHT, mBorderRadius);
            } else {
                roundedDrawable.setCornerRadius(Corner.BOTTOM_RIGHT, mBorderRadii[2]);
            }
            if (YogaConstants.isUndefined(mBorderRadii[3])) {
                roundedDrawable.setCornerRadius(Corner.BOTTOM_LEFT, mBorderRadius);
            } else {
                roundedDrawable.setCornerRadius(Corner.BOTTOM_LEFT, mBorderRadii[3]);
            }

        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable ld = (LayerDrawable) drawable;
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }
    }

}
