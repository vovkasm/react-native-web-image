package org.vovkasm.WebImage;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.Spacing;
import com.facebook.react.uimanager.UIViewOperationQueue;
import com.facebook.yoga.YogaConstants;

import java.util.Arrays;

class WebImageShadowNode extends LayoutShadowNode {
    private float[] mBorderWidths = new float[Spacing.ALL+1];

    WebImageShadowNode() {
        super();
        Arrays.fill(mBorderWidths, YogaConstants.UNDEFINED);
    }

    @Override
    public void setBorder(int spacingType, float borderWidth) {
        super.setBorder(spacingType, borderWidth);
        mBorderWidths[spacingType] = borderWidth;
    }

    private float getEffectiveBorderWidth(int spacing) {
        float width = mBorderWidths[spacing];
        if (YogaConstants.isUndefined(width)) width = mBorderWidths[Spacing.ALL];
        if (YogaConstants.isUndefined(width)) width = 0.0f;
        return width;
    }

    @Override
    public void onCollectExtraUpdates(UIViewOperationQueue uiViewOperationQueue) {
        super.onCollectExtraUpdates(uiViewOperationQueue);
        ShadowBoxMetrics bm = new ShadowBoxMetrics(
                getEffectiveBorderWidth(Spacing.START),
                getEffectiveBorderWidth(Spacing.TOP),
                getEffectiveBorderWidth(Spacing.END),
                getEffectiveBorderWidth(Spacing.BOTTOM),
                getPadding(Spacing.LEFT),
                getPadding(Spacing.TOP),
                getPadding(Spacing.RIGHT),
                getPadding(Spacing.BOTTOM)
        );
        uiViewOperationQueue.enqueueUpdateExtraData(getReactTag(), bm);
    }

}
