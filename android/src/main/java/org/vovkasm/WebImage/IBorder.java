package org.vovkasm.WebImage;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

interface IBorder {
    void setMetrics(BoxMetrics metrics);

    void draw(@NonNull Canvas canvas);
}
