package org.vovkasm.WebImage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class Border {
    public static final int DEFAULT_COLOR = Color.TRANSPARENT;

    private final RectF mBounds = new RectF();
    private boolean mBoundsValid = false;
    private final RectF mInnerRect = new RectF();
    private boolean mInnerRectValid = false;
    private final float[] mWidths = new float[]{0f, 0f, 0f, 0f};
    private final float[] mRadii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private final float[] mInnerRadii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    @ColorInt
    private final int[] mColors = new int[]{DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR};

    private final Paint mPaint;

    private final Path tmpPath = new Path();

    public Border() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    public void setRect(final RectF rect) {
        mBounds.set(rect);
        mBoundsValid = true;
        mInnerRectValid = false;
    }

    public RectF getRect() {
        updateRects();
        return mBounds;
    }

    public void setInnerRect(final RectF rect) {
        mInnerRect.set(rect);
        mBoundsValid = false;
        mInnerRectValid = true;
    }

    public RectF getInnerRect() {
        updateRects();
        return mInnerRect;
    }

    public Path getInnerPath() {
        updateRects();
        Path path = new Path();
        path.addRoundRect(mInnerRect, mInnerRadii, Path.Direction.CW);
        return path;
    }

    public void setRadii(float tl, float tr, float br, float bl) {
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

    public void setWidths(float ls, float ts, float rs, float bs) {
        mWidths[0] = ls;
        mWidths[1] = ts;
        mWidths[2] = rs;
        mWidths[3] = bs;
        update();
    }

    public void setColors(@ColorInt final int lc, @ColorInt final int tc, @ColorInt final int rc, @ColorInt final int bc) {
        mColors[0] = lc;
        mColors[1] = tc;
        mColors[2] = rc;
        mColors[3] = bc;
    }

    private void updateRects() {
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

    private void update() {
        updateRects();

        mInnerRadii[0] = Math.max(mRadii[0] - mWidths[0], 0f);
        mInnerRadii[1] = Math.max(mRadii[1] - mWidths[1], 0f);
        mInnerRadii[2] = Math.max(mRadii[2] - mWidths[2], 0f);
        mInnerRadii[3] = Math.max(mRadii[3] - mWidths[1], 0f);
        mInnerRadii[4] = Math.max(mRadii[4] - mWidths[2], 0f);
        mInnerRadii[5] = Math.max(mRadii[5] - mWidths[3], 0f);
        mInnerRadii[6] = Math.max(mRadii[6] - mWidths[0], 0f);
        mInnerRadii[7] = Math.max(mRadii[7] - mWidths[3], 0f);
    }

    public void draw(@NonNull Canvas canvas) {
        for (Side side : Side.values()) {
            tmpPath.rewind();
            mPaint.setColor(mColors[side.index]);
            calcBorderSegmentPath(tmpPath, side);
            canvas.drawPath(tmpPath, mPaint);
        }
    }

    private void calcBorderSegmentPath(Path path, Side side) {
        Corner corner2 = Corner.ALL[side.index];
        Corner corner1 = corner2.prev();
        float splitAngle1 = getSplitAngleForCorner(corner1);
        float splitAngle2 = getSplitAngleForCorner(corner2);
        float normalAngle = 270f + corner1.index * 90f;
        RectF oval = new RectF();
        calcOvalRect(oval, mBounds, mRadii, corner1);
        path.arcTo(oval, normalAngle - 90f + splitAngle1, 90f - splitAngle1);
        calcOvalRect(oval, mBounds, mRadii, corner2);
        path.arcTo(oval, normalAngle, splitAngle2);
        calcOvalRect(oval, mInnerRect, mInnerRadii, corner2);
        path.arcTo(oval, normalAngle + splitAngle2, -splitAngle2);
        calcOvalRect(oval, mInnerRect, mInnerRadii, corner1);
        path.arcTo(oval, normalAngle, -(90f - splitAngle1));
        path.close();
    }

    private float getSplitAngleForCorner(Corner corner) {
        return (float) Math.toDegrees(Math.atan2(mWidths[corner.index], mWidths[corner.next().index]));
    }

    private void calcOvalRect(RectF out, RectF rect, float[] radii, Corner corner) {
        switch (corner) {
            case TOP_LEFT:
                out.set(rect.left, rect.top, rect.left + 2f * radii[0], rect.top + 2f * radii[1]);
                break;
            case TOP_RIGHT:
                out.set(rect.right - 2f * radii[2], rect.top, rect.right, rect.top + 2f * radii[3]);
                break;
            case BOTTOM_RIGHT:
                out.set(rect.right - 2f * radii[4], rect.bottom - 2f * radii[5], rect.right, rect.bottom);
                break;
            case BOTTOM_LEFT:
                out.set(rect.left, rect.bottom - 2f * radii[7], rect.left + 2f * radii[6], rect.bottom);
                break;
        }
    }
}