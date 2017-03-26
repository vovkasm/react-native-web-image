package org.vovkasm.WebImage;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

interface IBorder {
    void setRect(RectF rect);
    void setRect(Rect rect);

    void setInnerRect(RectF rect);
    void setInnerRect(Rect rect);

    RectF getInnerRect();

    Path getInnerPath();

    void draw(@NonNull Canvas canvas);
}
