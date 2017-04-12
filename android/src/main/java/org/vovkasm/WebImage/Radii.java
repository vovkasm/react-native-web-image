package org.vovkasm.WebImage;

import android.support.annotation.IntDef;

import com.facebook.react.uimanager.FloatUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

class Radii {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TOP_LEFT_WIDTH, TOP_LEFT_HEIGHT,
            TOP_RIGHT_WIDTH, TOP_RIGHT_HEIGHT,
            BOTTOM_LEFT_WIDTH, BOTTOM_LEFT_HEIGHT,
            BOTTOM_RIGHT_WIDTH, BOTTOM_RIGHT_HEIGHT
    })
    @interface Corner {}
    static final int TOP_LEFT_WIDTH = 0;
    static final int TOP_LEFT_HEIGHT = 1;
    static final int TOP_RIGHT_WIDTH = 2;
    static final int TOP_RIGHT_HEIGHT = 3;
    static final int BOTTOM_RIGHT_WIDTH = 4;
    static final int BOTTOM_RIGHT_HEIGHT = 5;
    static final int BOTTOM_LEFT_WIDTH = 6;
    static final int BOTTOM_LEFT_HEIGHT = 7;

    private float[] mRadii = new float[8];

    Radii() {
        Arrays.fill(mRadii, 0f);
    }

    void set(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        mRadii[TOP_LEFT_WIDTH] = topLeft;
        mRadii[TOP_LEFT_HEIGHT] = topLeft;
        mRadii[TOP_RIGHT_WIDTH] = topRight;
        mRadii[TOP_RIGHT_HEIGHT] = topRight;
        mRadii[BOTTOM_RIGHT_WIDTH] = bottomRight;
        mRadii[BOTTOM_RIGHT_HEIGHT] = bottomRight;
        mRadii[BOTTOM_LEFT_WIDTH] = bottomLeft;
        mRadii[BOTTOM_LEFT_HEIGHT] = bottomLeft;
    }

    boolean isEqual4(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        return FloatUtil.floatsEqual(mRadii[TOP_LEFT_WIDTH], topLeft)
                && FloatUtil.floatsEqual(mRadii[TOP_LEFT_HEIGHT], topLeft)
                && FloatUtil.floatsEqual(mRadii[TOP_RIGHT_WIDTH], topRight)
                && FloatUtil.floatsEqual(mRadii[TOP_RIGHT_HEIGHT], topRight)
                && FloatUtil.floatsEqual(mRadii[BOTTOM_RIGHT_WIDTH], bottomRight)
                && FloatUtil.floatsEqual(mRadii[BOTTOM_RIGHT_HEIGHT], bottomRight)
                && FloatUtil.floatsEqual(mRadii[BOTTOM_LEFT_WIDTH], bottomLeft)
                && FloatUtil.floatsEqual(mRadii[BOTTOM_LEFT_HEIGHT], bottomLeft);
    }

    void set(final Radii radii) {
        System.arraycopy(radii.mRadii, 0, mRadii, 0, 8);
    }

    void shrink(float left, float top, float right, float bottom) {
        mRadii[TOP_LEFT_WIDTH] = Math.max(mRadii[TOP_LEFT_WIDTH] - left, 0f);
        mRadii[TOP_LEFT_HEIGHT] = Math.max(mRadii[TOP_LEFT_HEIGHT] - top, 0f);
        mRadii[TOP_RIGHT_WIDTH] = Math.max(mRadii[TOP_RIGHT_WIDTH] - right, 0f);
        mRadii[TOP_RIGHT_HEIGHT] = Math.max(mRadii[TOP_RIGHT_HEIGHT] - top, 0f);
        mRadii[BOTTOM_RIGHT_WIDTH] = Math.max(mRadii[BOTTOM_RIGHT_WIDTH] - right, 0f);
        mRadii[BOTTOM_RIGHT_HEIGHT] = Math.max(mRadii[BOTTOM_RIGHT_HEIGHT] - bottom, 0f);
        mRadii[BOTTOM_LEFT_WIDTH] = Math.max(mRadii[BOTTOM_LEFT_WIDTH] - left, 0f);
        mRadii[BOTTOM_LEFT_HEIGHT] = Math.max(mRadii[BOTTOM_LEFT_HEIGHT] - bottom, 0f);
    }

    float getRadius(@Corner int corner) {
        return mRadii[corner];
    }

    boolean isZero() {
        for (int i = 0; i < 8; i++) {
            if (!FloatUtil.floatsEqual(mRadii[i], 0f)) return true;
        }
        return false;
    }

    float[] asArray() {
        return mRadii;
    }
}
