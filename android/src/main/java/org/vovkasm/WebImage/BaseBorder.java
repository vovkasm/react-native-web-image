package org.vovkasm.WebImage;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

abstract class BaseBorder implements IBorder {
    final Paint mPaint;
    final RectF mBounds = new RectF();
    final RectF mInnerRect = new RectF();

    final float[] mWidths = new float[]{0f, 0f, 0f, 0f};
    final float[] mRadii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    final float[] mInnerRadii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    final Path mBorderPath = new Path();
    final Path mInnerPath = new Path();

    boolean mInnerRectValid = false;
    boolean mBoundsValid = false;


    BaseBorder() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    public void setRect(final RectF rect) {
        mBounds.set(rect);
        mBoundsValid = true;
        mInnerRectValid = false;
    }

    public void setRect(final Rect rect) {
        mBounds.set(rect);
        mBoundsValid = true;
        mInnerRectValid = false;
    }


    public void setInnerRect(final RectF rect) {
        mInnerRect.set(rect);
        mBoundsValid = false;
        mInnerRectValid = true;
    }

    public void setInnerRect(final Rect rect) {
        mInnerRect.set(rect);
        mBoundsValid = false;
        mInnerRectValid = true;
    }

    public RectF getInnerRect() {
        updateRects();
        return mInnerRect;
    }

    public Path getInnerPath() {
        update();
        return mInnerPath;
    }

    void setRadii(float tl, float tr, float br, float bl) {
        mRadii[0] = tl;
        mRadii[1] = tl;
        mRadii[2] = tr;
        mRadii[3] = tr;
        mRadii[4] = br;
        mRadii[5] = br;
        mRadii[6] = bl;
        mRadii[7] = bl;
        update();
    }

    void setWidths(float ls, float ts, float rs, float bs) {
        mWidths[0] = ls;
        mWidths[1] = ts;
        mWidths[2] = rs;
        mWidths[3] = bs;
        update();
    }

    void updateRects() {
        if (!mBoundsValid && mInnerRectValid) {
            mBounds.left = mInnerRect.left - mWidths[0];
            mBounds.top = mInnerRect.top - mWidths[1];
            mBounds.right = mInnerRect.right + mWidths[2];
            mBounds.bottom = mInnerRect.bottom + mWidths[3];
            mBoundsValid = true;
        }
        if (mBoundsValid && !mInnerRectValid) {
            mInnerRect.left = mBounds.left + mWidths[0];
            mInnerRect.top = mBounds.top + mWidths[1];
            mInnerRect.right = mBounds.right - mWidths[2];
            mInnerRect.bottom = mBounds.bottom - mWidths[3];
            mInnerRectValid = true;
        }
    }

    void update() {
        updateRects();

        mInnerRadii[0] = Math.max(mRadii[0] - mWidths[0], 0f);
        mInnerRadii[1] = Math.max(mRadii[1] - mWidths[1], 0f);
        mInnerRadii[2] = Math.max(mRadii[2] - mWidths[2], 0f);
        mInnerRadii[3] = Math.max(mRadii[3] - mWidths[1], 0f);
        mInnerRadii[4] = Math.max(mRadii[4] - mWidths[2], 0f);
        mInnerRadii[5] = Math.max(mRadii[5] - mWidths[3], 0f);
        mInnerRadii[6] = Math.max(mRadii[6] - mWidths[0], 0f);
        mInnerRadii[7] = Math.max(mRadii[7] - mWidths[3], 0f);

        mBorderPath.rewind();
        mBorderPath.addRoundRect(mBounds, mRadii, Path.Direction.CW);
        mBorderPath.addRoundRect(mInnerRect, mInnerRadii, Path.Direction.CCW);
        mInnerPath.rewind();
        mInnerPath.addRoundRect(mInnerRect, mInnerRadii, Path.Direction.CW);
    }

}
