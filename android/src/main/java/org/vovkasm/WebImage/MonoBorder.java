package org.vovkasm.WebImage;

import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

class MonoBorder extends BaseBorder {

    public void setColor(@ColorInt final int color) {
        mPaint.setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(mBoxMetrics.getBorderPath(), mPaint);
    }
}
