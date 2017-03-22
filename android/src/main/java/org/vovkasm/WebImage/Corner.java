package org.vovkasm.WebImage;

enum Corner {
    TOP_LEFT(0), TOP_RIGHT(1), BOTTOM_RIGHT(2), BOTTOM_LEFT(3);

    public final int index;

    Corner(final int index) {
        this.index = index;
    }

    public Corner next() {
        final int i = index < 3 ? index + 1 : 0;
        return ALL[i];
    }

    public Corner prev() {
        final int i = index > 0 ? index - 1 : 3;
        return ALL[i];
    }

    public static final Corner[] ALL = new Corner[]{TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT};
}
