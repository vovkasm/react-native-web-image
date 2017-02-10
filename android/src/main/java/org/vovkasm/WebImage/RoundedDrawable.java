package org.vovkasm.WebImage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView.ScaleType;

public class RoundedDrawable extends Drawable {
    public static final String TAG = "RoundedDrawable";

    private final RectF mBounds = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final Bitmap mBitmap;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final Matrix mShaderMatrix = new Matrix();
    private final Border mBorder = new Border();

    private boolean mRebuildShader = true;

    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    public RoundedDrawable(Bitmap bitmap) {
        mBitmap = bitmap;

        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);
    }

    public static RoundedDrawable fromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return new RoundedDrawable(bitmap);
        } else {
            return null;
        }
    }

    public static Drawable fromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                return drawable;
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable) drawable;
                for (int i = 0, num = ld.getNumberOfLayers(); i < num; i++) {
                    Drawable d = ld.getDrawable(i);
                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d));
                }
                return ld;
            }

            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundedDrawable(bm);
            }
        }
        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Failed to create bitmap from drawable!");
            bitmap = null;
        }

        return bitmap;
    }

    private void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;

        mBorder.setRect(mBounds);

        mShaderMatrix.reset();
        switch (mScaleType) {
            case CENTER:
                mShaderMatrix.setTranslate((int) ((mBounds.width() - mBitmapWidth) * 0.5f + 0.5f),
                        (int) ((mBounds.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;

            case CENTER_CROP:
                dx = 0;
                dy = 0;
                final RectF rect = mBorder.getInnerRect();

                if (mBitmapWidth * rect.height() > rect.width() * mBitmapHeight) {
                    scale = rect.height() / (float) mBitmapHeight;
                    dx = (rect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = rect.width() / (float) mBitmapWidth;
                    dy = (rect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
                break;

            case CENTER_INSIDE:
                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(mBounds.width() / (float) mBitmapWidth,
                            mBounds.height() / (float) mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);

                mShaderMatrix.mapRect(mBorder.getInnerRect());
                mShaderMatrix.setRectToRect(mBitmapRect, mBorder.getInnerRect(), Matrix.ScaleToFit.FILL);
                break;

            default:
            case FIT_CENTER:
                mShaderMatrix.setRectToRect(mBitmapRect, mBorder.getInnerRect(), Matrix.ScaleToFit.CENTER);
                break;

            case FIT_END:
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorder.getInnerRect());
                mShaderMatrix.setRectToRect(mBitmapRect, mBorder.getInnerRect(), Matrix.ScaleToFit.FILL);
                break;

            case FIT_START:
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorder.getInnerRect());
                mShaderMatrix.setRectToRect(mBitmapRect, mBorder.getInnerRect(), Matrix.ScaleToFit.FILL);
                break;

            case FIT_XY:
                mShaderMatrix.setRectToRect(mBitmapRect, mBorder.getInnerRect(), Matrix.ScaleToFit.FILL);
                break;
        }
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);

        mBounds.set(bounds);

        updateShaderMatrix();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mRebuildShader) {
            BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bitmapShader.setLocalMatrix(mShaderMatrix);
            mBitmapPaint.setShader(bitmapShader);
            mRebuildShader = false;
        }

        canvas.drawPath(mBorder.getInnerPath(), mBitmapPaint);

        mBorder.draw(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getAlpha() {
        return mBitmapPaint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mBitmapPaint.getColorFilter();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mBitmapPaint.setDither(dither);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mBitmapPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public void setBorderRadii(float tl, float tr, float br, float bl) {
        mBorder.setRadii(tl, tr, br, bl);
    }

    public void setBorderWidths(float ls, float ts, float rs, float bs) {
        mBorder.setWidths(ls, ts, rs, bs);
    }

    public void setBorderColors(@ColorInt final int lc, @ColorInt final int tc, @ColorInt final int rc, @ColorInt final int bc) {
        mBorder.setColors(lc, tc, rc, bc);
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_CENTER;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }
    }

}