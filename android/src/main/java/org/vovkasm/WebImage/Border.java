package org.vovkasm.WebImage;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

public interface Border {
    void setRect(RectF rect);

    void setInnerRect(RectF rect);

    RectF getInnerRect();

    Path getInnerPath();

    void draw(@NonNull Canvas canvas);
}
