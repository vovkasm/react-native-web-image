package org.vovkasm.WebImage;

import android.graphics.Rect;
import android.graphics.RectF;

import com.facebook.react.uimanager.FloatUtil;

class BoxMetrics {
    float width = 0f;
    float height = 0f;

    float borderLeft = 0f;
    float borderTop = 0f;
    float borderRight = 0f;
    float borderBottom = 0f;

    float paddingLeft = 0f;
    float paddingTop = 0f;
    float paddingRight = 0f;
    float paddingBottom = 0f;

    private boolean dirty = false;

    private Rect borderRect = new Rect();
    private Rect paddingRect = new Rect();
    private Rect contentRect = new Rect();

    private RectF contentRectF = new RectF();

    void set(ShadowBoxMetrics sm) {
        boolean isSame = FloatUtil.floatsEqual(width, sm.width)
                && FloatUtil.floatsEqual(height, sm.height)
                && FloatUtil.floatsEqual(borderLeft, sm.borderLeft)
                && FloatUtil.floatsEqual(borderTop, sm.borderTop)
                && FloatUtil.floatsEqual(borderRight, sm.borderRight)
                && FloatUtil.floatsEqual(borderBottom, sm.borderBottom)
                && FloatUtil.floatsEqual(paddingLeft, sm.paddingLeft)
                && FloatUtil.floatsEqual(paddingTop, sm.paddingTop)
                && FloatUtil.floatsEqual(paddingRight, sm.paddingRight)
                && FloatUtil.floatsEqual(paddingBottom, sm.paddingBottom);
        if (isSame) return;
        width = sm.width;
        height = sm.height;
        borderLeft = sm.borderLeft;
        borderTop = sm.borderTop;
        borderRight = sm.borderRight;
        borderBottom = sm.borderBottom;
        paddingLeft = sm.paddingLeft;
        paddingTop = sm.paddingTop;
        paddingRight = sm.paddingRight;
        paddingBottom = sm.paddingBottom;
        contentRectF.set(borderLeft + paddingLeft, borderTop + paddingTop, width - paddingRight - borderRight, height - paddingBottom + borderBottom);
        dirty = true;
    }

    void update() {
        if (dirty) {
            contentRectF.round(contentRect);
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

            dirty = false;
        }
    }

    float getContentWidth() { return contentRectF.width(); }
    float getContentHeight() { return contentRectF.height(); }
    RectF getContentRectF() { return contentRectF; }

    Rect getContentRect() {
        update();
        return contentRect;
    }

    Rect getBorderRect() {
        update();
        return borderRect;
    }

    Rect getPaddingRect() {
        update();
        return paddingRect;
    }

    void ajustContentSize(float realWidth, float realHeight) {
        float cw = contentRectF.width();
        float ch = contentRectF.height();
        float w = Math.min(cw, realWidth);
        float h = Math.min(ch, realHeight);

        if (w != cw) {
            float dx = (cw - w) * 0.5f;
            contentRectF.left += dx;
            contentRectF.right -= dx;
            dirty = true;
        }
        if (h != ch) {
            float dy = (ch - h) * 0.5f;
            contentRectF.top += dy;
            contentRectF.bottom -= dy;
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
