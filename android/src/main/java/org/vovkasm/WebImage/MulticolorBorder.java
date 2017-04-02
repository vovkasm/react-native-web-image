package org.vovkasm.WebImage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

class MulticolorBorder extends BaseBorder {
    private static final int DEFAULT_COLOR = Color.TRANSPARENT;

    @ColorInt
    private final int[] mColors = new int[]{DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR};

    private final Path tmpPath = new Path();
    private final RectF tmpRect = new RectF();

    void setColors(@ColorInt final int lc, @ColorInt final int tc, @ColorInt final int rc, @ColorInt final int bc) {
        mColors[0] = lc;
        mColors[1] = tc;
        mColors[2] = rc;
        mColors[3] = bc;
    }

    @Override
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
        final RectF borderRect = mBoxMetrics.getBorderRect();
        final Radii borderRadii = mBoxMetrics.getBorderRadii();
        final RectF paddingRect = mBoxMetrics.getPaddingRect();
        final Radii paddingRadii = mBoxMetrics.getPaddingRadii();
        calcOvalRect(tmpRect, borderRect, borderRadii, corner1);
        path.arcTo(tmpRect, normalAngle - 90f + splitAngle1, 90f - splitAngle1);
        calcOvalRect(tmpRect, borderRect, borderRadii, corner2);
        path.arcTo(tmpRect, normalAngle, splitAngle2);
        calcOvalRect(tmpRect, paddingRect, paddingRadii, corner2);
        path.arcTo(tmpRect, normalAngle + splitAngle2, -splitAngle2);
        calcOvalRect(tmpRect, paddingRect, paddingRadii, corner1);
        path.arcTo(tmpRect, normalAngle, -(90f - splitAngle1));
        path.close();
    }

    private float getSplitAngleForCorner(Corner corner) {
        switch (corner) {
            case TOP_LEFT:
                return (float) Math.toDegrees(Math.atan2(mBoxMetrics.borderLeft, mBoxMetrics.borderTop));
            case TOP_RIGHT:
                return (float) Math.toDegrees(Math.atan2(mBoxMetrics.borderTop, mBoxMetrics.borderRight));
            case BOTTOM_RIGHT:
                return (float) Math.toDegrees(Math.atan2(mBoxMetrics.borderRight, mBoxMetrics.borderBottom));
            case BOTTOM_LEFT:
                return (float) Math.toDegrees(Math.atan2(mBoxMetrics.borderBottom, mBoxMetrics.borderLeft));
        }
        return 0f;
    }

    private void calcOvalRect(RectF out, RectF rect, final Radii radii, Corner corner) {
        switch (corner) {
            case TOP_LEFT:
                out.set(rect.left,
                        rect.top,
                        rect.left + 2f * radii.getRadius(Radii.TOP_LEFT_WIDTH),
                        rect.top + 2f * radii.getRadius(Radii.TOP_LEFT_HEIGHT));
                break;
            case TOP_RIGHT:
                out.set(rect.right - 2f * radii.getRadius(Radii.TOP_RIGHT_WIDTH),
                        rect.top,
                        rect.right,
                        rect.top + 2f * radii.getRadius(Radii.TOP_RIGHT_HEIGHT));
                break;
            case BOTTOM_RIGHT:
                out.set(rect.right - 2f * radii.getRadius(Radii.BOTTOM_RIGHT_WIDTH),
                        rect.bottom - 2f * radii.getRadius(Radii.BOTTOM_RIGHT_HEIGHT),
                        rect.right,
                        rect.bottom);
                break;
            case BOTTOM_LEFT:
                out.set(rect.left,
                        rect.bottom - 2f * radii.getRadius(Radii.BOTTOM_LEFT_HEIGHT),
                        rect.left + 2f * radii.getRadius(Radii.BOTTOM_LEFT_WIDTH),
                        rect.bottom);
                break;
        }
    }

}
