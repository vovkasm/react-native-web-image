package org.vovkasm.WebImage;

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

    boolean dirty = false;

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
        dirty = true;
    }

    boolean hasBorder() {
        return !(FloatUtil.floatsEqual(borderLeft, 0f)
                && FloatUtil.floatsEqual(borderTop, 0f)
                && FloatUtil.floatsEqual(borderRight, 0f)
                && FloatUtil.floatsEqual(borderBottom, 0f)
        );
    }
}
