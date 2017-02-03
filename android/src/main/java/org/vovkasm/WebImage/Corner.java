package org.vovkasm.WebImage;

public enum Corner {
    TOP_LEFT(0),
    TOP_RIGHT(1),
    BOTTOM_RIGHT(2),
    BOTTOM_LEFT(3);

    public int index;

    Corner(final int index) {
        this.index = index;
    }
}