package org.vovkasm.WebImage;

import androidx.annotation.NonNull;

import android.graphics.Canvas;

interface IBorder {
    void setMetrics(BoxMetrics metrics);

    void draw(@NonNull Canvas canvas);
}
