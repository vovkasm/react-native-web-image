package org.vovkasm.WebImage;

class ShadowBoxMetrics {
    float width;
    float height;

    float borderLeft;
    float borderTop;
    float borderRight;
    float borderBottom;

    float paddingLeft;
    float paddingTop;
    float paddingRight;
    float paddingBottom;

    ShadowBoxMetrics(float w, float h, float bl, float bt, float br, float bb, float pl, float pt, float pr, float pb) {
        width = w;
        height = h;
        borderLeft = bl;
        borderTop = bt;
        borderRight = br;
        borderBottom = bb;
        paddingLeft = pl;
        paddingTop = pt;
        paddingRight = pr;
        paddingBottom = pb;
    }
}
