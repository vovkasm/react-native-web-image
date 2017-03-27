package org.vovkasm.WebImage;

class ShadowBoxMetrics {
    private float mWidth;
    private float mHeight;

    private float mBorderLeft;
    private float mBorderTop;
    private float mBorderRight;
    private float mBorderBottom;

    private float mPaddingLeft;
    private float mPaddingTop;
    private float mPaddingRight;
    private float mPaddingBottom;

    ShadowBoxMetrics(float w, float h, float bl, float bt, float br, float bb, float pl, float pt, float pr, float pb) {
        mWidth = w;
        mHeight = h;
        mBorderLeft = bl;
        mBorderTop = bt;
        mBorderRight = br;
        mBorderBottom = bb;
        mPaddingLeft = pl;
        mPaddingTop = pt;
        mPaddingRight = pr;
        mPaddingBottom = pb;
    }

    float getBorderLeft() { return mBorderLeft; }
    float getBorderTop() { return mBorderTop; }
    float getBorderRight() { return mBorderRight; }
    float getBorderBottom() { return mBorderBottom; }

    float getWidth() { return mWidth; }
    float getHeight() { return mHeight; }

    float getPaddingLeft() { return mPaddingLeft; }
    float getPaddingTop() { return mPaddingTop; }
    float getPaddingRight() { return mPaddingRight; }
    float getPaddingBottom() { return mPaddingBottom; }

    boolean hasBorder() {
        return mBorderLeft != 0f || mBorderTop != 0f || mBorderRight != 0f || mBorderBottom != 0f;
    }

    boolean equalsToBoxMetrics(ShadowBoxMetrics bm) {
        return bm != null && bm.mWidth == mWidth && bm.mHeight == mHeight &&
                bm.mBorderLeft == mBorderLeft && bm.mBorderTop == mBorderTop && bm.mBorderRight == mBorderRight && bm.mBorderBottom == mBorderBottom &&
                bm.mPaddingLeft == mPaddingLeft && bm.mPaddingTop == mPaddingTop && bm.mPaddingRight == mPaddingRight && bm.mPaddingBottom == mPaddingBottom;

    }
}
