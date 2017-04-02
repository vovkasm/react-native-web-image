package org.vovkasm.WebImage;

import android.graphics.Paint;

abstract class BaseBorder implements IBorder {
    final Paint mPaint = new Paint();

    BoxMetrics mBoxMetrics = null;

    BaseBorder() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setMetrics(BoxMetrics metrics) {
        mBoxMetrics = metrics;
    }
}
