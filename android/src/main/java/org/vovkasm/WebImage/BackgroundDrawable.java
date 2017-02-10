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
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView.ScaleType;

public class BackgroundDrawable extends Drawable {
    public static final String TAG = "BackgroundDrawable";

    private final RectF mBounds = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final Bitmap mBitmap;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final Matrix mShaderMatrix = new Matrix();

    private RectF mInnerRect = new RectF();
    private Border mBorder;
    private boolean mDirty = true;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    public BackgroundDrawable(Bitmap bitmap) {
        mBitmap = bitmap;

        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);
    }

    public static BackgroundDrawable fromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return new BackgroundDrawable(bitmap);
        } else {
            return null;
        }
    }

    public static Drawable fromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BackgroundDrawable) {
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
                return new BackgroundDrawable(bm);
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

    private void update() {
        float scale;
        float dx;
        float dy;

        if (mBorder != null) {
            mBorder.setRect(mBounds);
            mInnerRect.set(mBorder.getInnerRect());
        } else {
            mInnerRect.set(mBounds);
        }

        mShaderMatrix.reset();
        switch (mScaleType) {
            // contain
            default:
            case FIT_CENTER:
                mShaderMatrix.setRectToRect(mBitmapRect, mInnerRect, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mInnerRect, mBitmapRect);
                if (mBorder != null) mBorder.setInnerRect(mInnerRect);
                break;
            // cover
            case CENTER_CROP:
                dx = 0;
                dy = 0;
                if (mBitmapWidth * mInnerRect.height() > mInnerRect.width() * mBitmapHeight) {
                    scale = mInnerRect.height() / (float) mBitmapHeight;
                    dx = (mInnerRect.width() - mBitmapWidth * scale) * 0.5f + 0.5f;
                } else {
                    scale = mInnerRect.width() / (float) mBitmapWidth;
                    dy = (mInnerRect.height() - mBitmapHeight * scale) * 0.5f + 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) dx, (int) dy);
                break;
            // stretch
            case FIT_XY:
                mShaderMatrix.setRectToRect(mBitmapRect, mInnerRect, Matrix.ScaleToFit.FILL);
                break;
            // center
            case CENTER:
                dx = (mBounds.width() - mBitmapWidth) * 0.5f + 0.5f;
                dy = (mBounds.height() - mBitmapHeight) * 0.5f + 0.5f;
                mShaderMatrix.setTranslate(dx, dy);
                RectF tmpRect = new RectF();
                mShaderMatrix.mapRect(tmpRect, mBitmapRect);
                mInnerRect.intersect(tmpRect);
                if (mBorder != null) mBorder.setInnerRect(mInnerRect);
                break;
        }

        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(mShaderMatrix);
        mBitmapPaint.setShader(bitmapShader);

        mDirty = false;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);

        mBounds.set(bounds);

        mDirty = true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mDirty) {
            update();
            mDirty = false;
        }

        if (mBorder == null) {
            canvas.drawRect(mInnerRect, mBitmapPaint);
        } else {
            canvas.drawPath(mBorder.getInnerPath(), mBitmapPaint);
            mBorder.draw(canvas);
        }
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

    public void setBorder(Border border) {
        mBorder = border;
        mDirty = true;
    }

    public Border getBorder() {
        return mBorder;
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
            mDirty = true;
        }
    }

}