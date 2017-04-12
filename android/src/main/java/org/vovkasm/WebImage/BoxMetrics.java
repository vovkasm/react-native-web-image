package org.vovkasm.WebImage;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

import com.facebook.react.uimanager.FloatUtil;

class BoxMetrics {
    private int layoutWidth = 0;
    private int layoutHeight = 0;

    float borderLeft = 0f;
    float borderTop = 0f;
    float borderRight = 0f;
    float borderBottom = 0f;

    private float paddingLeft = 0f;
    private float paddingTop = 0f;
    private float paddingRight = 0f;
    private float paddingBottom = 0f;

    private int imageWidth = 0;
    private int imageHeight = 0;
    private @WebImageView.ScaleType int scaleType;

    private Radii borderRadii = new Radii();
    private Radii paddingRadii = new Radii();
    private Radii contentRadii = new Radii();

    private boolean dirty = false;

    private RectF borderRect = new RectF();
    private RectF paddingRect = new RectF();
    private RectF contentRect = new RectF();

    private Matrix contentMatrix = new Matrix();

    private Path borderPath = new Path();
    private Path contentPath = new Path();

    BoxMetrics (@WebImageView.ScaleType int scaleType) {
        this.scaleType = scaleType;
    }

    void setShadowMetrics(ShadowBoxMetrics sm) {
        boolean isSame = FloatUtil.floatsEqual(borderLeft, sm.borderLeft)
                && FloatUtil.floatsEqual(borderTop, sm.borderTop)
                && FloatUtil.floatsEqual(borderRight, sm.borderRight)
                && FloatUtil.floatsEqual(borderBottom, sm.borderBottom)
                && FloatUtil.floatsEqual(paddingLeft, sm.paddingLeft)
                && FloatUtil.floatsEqual(paddingTop, sm.paddingTop)
                && FloatUtil.floatsEqual(paddingRight, sm.paddingRight)
                && FloatUtil.floatsEqual(paddingBottom, sm.paddingBottom);
        if (isSame) return;
        borderLeft = sm.borderLeft;
        borderTop = sm.borderTop;
        borderRight = sm.borderRight;
        borderBottom = sm.borderBottom;
        paddingLeft = sm.paddingLeft;
        paddingTop = sm.paddingTop;
        paddingRight = sm.paddingRight;
        paddingBottom = sm.paddingBottom;
        dirty = true;
    }

    void setSize(int width, int height) {
        if (width == layoutWidth && height == layoutHeight) return;
        layoutWidth = width;
        layoutHeight = height;
        dirty = true;
    }

    void setRadii(float tl, float tr, float br, float bl) {
        if (borderRadii.isEqual4(tl, tr, br, bl)) return;
        borderRadii.set(tl, tr, br, bl);
        dirty = true;
    }

    void setScaleType(@WebImageView.ScaleType int scaleType) {
        if (this.scaleType == scaleType) return;
        this.scaleType = scaleType;
        dirty = true;
    }

    void setImageSize(int width, int height) {
        if (imageWidth == width && imageHeight == height) return;
        imageWidth = width;
        imageHeight = height;
        dirty = true;
    }

    private void update() {
        if (dirty) {
            updateRects();

            paddingRadii.set(borderRadii);
            paddingRadii.shrink(borderLeft, borderTop, borderRight, borderBottom);
            contentRadii.set(paddingRadii);
            contentRadii.shrink(paddingLeft, paddingTop, paddingRight,paddingBottom);

            borderPath.rewind();
            borderPath.addRoundRect(borderRect, borderRadii.asArray(), Path.Direction.CW);
            borderPath.addRoundRect(paddingRect, paddingRadii.asArray(), Path.Direction.CCW);

            contentPath.rewind();
            contentPath.addRoundRect(contentRect, contentRadii.asArray(), Path.Direction.CW);

            dirty = false;
        }
    }

    private void updateRects() {
        borderRect.set(0f, 0f, layoutWidth, layoutHeight);
        insetRect(paddingRect, borderRect, borderLeft, borderTop, borderRight, borderBottom);
        insetRect(contentRect, paddingRect, paddingLeft, paddingTop, paddingRight, paddingBottom);

        contentMatrix.reset();

        final float availableWidth = contentRect.width();
        final float availableHeight = contentRect.height();
        if (scaleType == WebImageView.SCALE_STRETCH) {
            final float sx = availableWidth / imageWidth;
            final float sy = availableHeight / imageHeight;
            contentMatrix.setScale(sx, sy);
        } else if (scaleType == WebImageView.SCALE_CENTER) {
            final float dw = (availableWidth - imageWidth) * 0.5f;
            final float dh = (availableHeight - imageHeight) * 0.5f;
            float diffWidth = dw > 0 ? dw : 0f;
            float diffHeight = dh > 0 ? dh : 0f;
            contentRect.inset(diffWidth, diffHeight);
            paddingRect.inset(diffWidth, diffHeight);
            borderRect.inset(diffWidth, diffHeight);
            contentMatrix.postTranslate(dw, dh);
        } else if (scaleType == WebImageView.SCALE_COVER) {
            final float sx = availableWidth / imageWidth;
            final float sy = availableHeight / imageHeight;
            if (sx > sy) {
                float diff = (availableHeight - imageHeight * sx) * 0.5f;
                contentMatrix.setScale(sx, sx);
                contentMatrix.postTranslate(0f, diff);
            } else {
                float diff = (availableWidth - imageWidth * sy) * 0.5f;
                contentMatrix.setScale(sy, sy);
                contentMatrix.postTranslate(diff, 0f);
            }
        } else if (scaleType == WebImageView.SCALE_CONTAIN && imageWidth > 0f && imageHeight > 0f) {
            final float sx = availableWidth / imageWidth;
            final float sy = availableHeight / imageHeight;
            if (sx > sy) {
                float diff = (availableWidth - imageWidth * sy) * 0.5f;
                contentRect.inset(diff, 0f);
                paddingRect.inset(diff, 0f);
                borderRect.inset(diff, 0f);
                contentMatrix.setScale(sy, sy);
                contentMatrix.postTranslate(diff, 0f);
            } else {
                float diff = (availableHeight - imageHeight * sx) * 0.5f;
                contentRect.inset(0f, diff);
                paddingRect.inset(0f, diff);
                borderRect.inset(0f, diff);
                contentMatrix.setScale(sx, sx);
                contentMatrix.postTranslate(0f, diff);
            }
        }

        contentMatrix.postTranslate(borderLeft + paddingLeft, borderTop + paddingTop);
    }

    final Matrix getContentMatrix() {
        update();
        return contentMatrix;
    }

    final RectF getBorderRect() {
        update();
        return borderRect;
    }

    final RectF getPaddingRect() {
        update();
        return paddingRect;
    }

    final Radii getBorderRadii() {
        update();
        return borderRadii;
    }

    final Radii getPaddingRadii() {
        update();
        return paddingRadii;
    }

    final Path getContentPath() {
        update();
        return contentPath;
    }

    final Path getBorderPath() {
        update();
        return borderPath;
    }

    boolean hasBorder() {
        return !(FloatUtil.floatsEqual(borderLeft, 0f)
                && FloatUtil.floatsEqual(borderTop, 0f)
                && FloatUtil.floatsEqual(borderRight, 0f)
                && FloatUtil.floatsEqual(borderBottom, 0f)
        );
    }

    private static void insetRect(RectF dst, RectF src, float left, float top, float right, float bottom) {
        dst.set(src.left + left, src.top + top, src.right - right, src.bottom - bottom);
    }
}
