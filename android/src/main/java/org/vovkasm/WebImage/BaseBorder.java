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
    final Radii mRadii = new Radii();
    final Radii mInnerRadii = new Radii();

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
        mRadii.set(tl, tr, br, bl);
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

        mInnerRadii.set(mRadii);
        mInnerRadii.shrink(mWidths[0], mWidths[1], mWidths[2], mWidths[3]);

        mBorderPath.rewind();
        mBorderPath.addRoundRect(mBounds, mRadii.asArray(), Path.Direction.CW);
        mBorderPath.addRoundRect(mInnerRect, mInnerRadii.asArray(), Path.Direction.CCW);
        mInnerPath.rewind();
        mInnerPath.addRoundRect(mInnerRect, mInnerRadii.asArray(), Path.Direction.CW);
    }

}
