package org.vovkasm.WebImage;

enum Side {
    LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

    public final int index;

    Side(final int index) {
        this.index = index;
    }
}
