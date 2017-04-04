package org.vovkasm.WebImage;

import android.graphics.Path;
import android.graphics.RectF;

import com.facebook.react.uimanager.FloatUtil;

class BoxMetrics {
    private float layoutWidth = 0f;
    private float layoutHeight = 0f;

    float borderLeft = 0f;
    float borderTop = 0f;
    float borderRight = 0f;
    float borderBottom = 0f;

    float paddingLeft = 0f;
    float paddingTop = 0f;
    float paddingRight = 0f;
    float paddingBottom = 0f;

    private Radii borderRadii = new Radii();
    private Radii paddingRadii = new Radii();
    private Radii contentRadii = new Radii();

    private boolean dirty = false;

    private RectF borderRect = new RectF();
    private RectF paddingRect = new RectF();
    private RectF contentRect = new RectF();

    private Path borderPath = new Path();
    private Path contentPath = new Path();

    void setShadowMetrics(ShadowBoxMetrics sm) {
        boolean isSame = FloatUtil.floatsEqual(layoutWidth, sm.width)
                && FloatUtil.floatsEqual(layoutHeight, sm.height)
                && FloatUtil.floatsEqual(borderLeft, sm.borderLeft)
                && FloatUtil.floatsEqual(borderTop, sm.borderTop)
                && FloatUtil.floatsEqual(borderRight, sm.borderRight)
                && FloatUtil.floatsEqual(borderBottom, sm.borderBottom)
                && FloatUtil.floatsEqual(paddingLeft, sm.paddingLeft)
                && FloatUtil.floatsEqual(paddingTop, sm.paddingTop)
                && FloatUtil.floatsEqual(paddingRight, sm.paddingRight)
                && FloatUtil.floatsEqual(paddingBottom, sm.paddingBottom);
        if (isSame) return;
        layoutWidth = sm.width;
        layoutHeight = sm.height;
        borderLeft = sm.borderLeft;
        borderTop = sm.borderTop;
        borderRight = sm.borderRight;
        borderBottom = sm.borderBottom;
        paddingLeft = sm.paddingLeft;
        paddingTop = sm.paddingTop;
        paddingRight = sm.paddingRight;
        paddingBottom = sm.paddingBottom;
        contentRect.set(borderLeft + paddingLeft, borderTop + paddingTop, layoutWidth - paddingRight - borderRight, layoutHeight - paddingBottom + borderBottom);
        dirty = true;
    }

    void setRadii(float tl, float tr, float br, float bl) {
        borderRadii.set(tl, tr, br, bl);
        dirty = true;
    }

    void update() {
        if (dirty) {
            paddingRect.set(contentRect);
            paddingRect.left -= paddingLeft;
            paddingRect.top -= paddingTop;
            paddingRect.right += paddingRight;
            paddingRect.bottom += paddingBottom;
            borderRect.set(paddingRect);
            borderRect.left -= borderLeft;
            borderRect.top -= borderTop;
            borderRect.right += borderRight;
            borderRect.bottom += borderBottom;

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

    float getLayoutContentWidth() { return layoutWidth - (borderLeft + paddingLeft + paddingRight + borderRight); }
    float getLayoutContentHeight() { return layoutHeight - (borderTop + paddingTop + paddingBottom + borderBottom); }

    RectF getContentRect() {
        return contentRect;
    }

    RectF getBorderRect() {
        update();
        return borderRect;
    }

    RectF getPaddingRect() {
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

    final Radii getContentRadii() {
        update();
        return contentRadii;
    }

    final Path getContentPath() {
        update();
        return contentPath;
    }

    final Path getBorderPath() {
        update();
        return borderPath;
    }

    void ajustContentSize(float realWidth, float realHeight) {
        float cw = contentRect.width();
        float ch = contentRect.height();
        float w = Math.min(cw, realWidth);
        float h = Math.min(ch, realHeight);

        if (w != cw) {
            float dx = (cw - w) * 0.5f;
            contentRect.left += dx;
            contentRect.right -= dx;
            dirty = true;
        }
        if (h != ch) {
            float dy = (ch - h) * 0.5f;
            contentRect.top += dy;
            contentRect.bottom -= dy;
            dirty = true;
        }
    }

    boolean hasBorder() {
        return !(FloatUtil.floatsEqual(borderLeft, 0f)
                && FloatUtil.floatsEqual(borderTop, 0f)
                && FloatUtil.floatsEqual(borderRight, 0f)
                && FloatUtil.floatsEqual(borderBottom, 0f)
        );
    }
}
