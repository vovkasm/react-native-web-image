package org.vovkasm.WebImage;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
    public static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    public enum Corner {
        TOP_LEFT(0), TOP_RIGHT(1), BOTTOM_RIGHT(2), BOTTOM_LEFT(3);

        public int index;

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

        private static final Corner[] ALL = new Corner[] { TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT };
    }

    public enum Side {
        LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

        public int index;

        Side(final int index) {
            this.index = index;
        }
    }

    private final RectF mBounds = new RectF();
    private final RectF mDrawableRect = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final Bitmap mBitmap;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final RectF mBorderOuterRect = new RectF();
    private final RectF mBorderInnerRect = new RectF();
    private final Paint mBorderPaint;
    private final Matrix mShaderMatrix = new Matrix();

    private boolean mRebuildShader = true;

    private float[] mCornerRadii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private float[] mCornerInnerRadii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private float[] mBorderSizes = new float[]{0f, 0f, 0f, 0f};
    private
    @ColorInt
    int[] mBorderColors = new int[]{DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_COLOR};

    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    private int mColor = Color.TRANSPARENT;

    public RoundedDrawable(Bitmap bitmap) {
        mBitmap = bitmap;

        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColors[0]);
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

        mBorderOuterRect.set(mBounds);
        mBorderInnerRect.set(mBounds);
        mBorderInnerRect.left += mBorderSizes[0];
        mBorderInnerRect.top += mBorderSizes[1];
        mBorderInnerRect.right -= mBorderSizes[2];
        mBorderInnerRect.bottom -= mBorderSizes[3];

        switch (mScaleType) {
            case CENTER:

                mShaderMatrix.reset();
                mShaderMatrix.setTranslate((int) ((mBorderOuterRect.width() - mBitmapWidth) * 0.5f + 0.5f),
                        (int) ((mBorderOuterRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                break;

            case CENTER_CROP:
                mShaderMatrix.reset();

                dx = 0;
                dy = 0;

                if (mBitmapWidth * mBorderOuterRect.height() > mBorderOuterRect.width() * mBitmapHeight) {
                    scale = mBorderOuterRect.height() / (float) mBitmapHeight;
                    dx = (mBorderOuterRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mBorderOuterRect.width() / (float) mBitmapWidth;
                    dy = (mBorderOuterRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderSizes[0],
                        (int) (dy + 0.5f) + mBorderSizes[1]);
                break;

            case CENTER_INSIDE:
                mShaderMatrix.reset();

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

                mShaderMatrix.mapRect(mBorderInnerRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderInnerRect, Matrix.ScaleToFit.FILL);
                break;

            default:
            case FIT_CENTER:
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderInnerRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderInnerRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_END:
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderInnerRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderInnerRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_START:
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderInnerRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderInnerRect, Matrix.ScaleToFit.FILL);
                break;

            case FIT_XY:
                mShaderMatrix.reset();
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderInnerRect, Matrix.ScaleToFit.FILL);
                break;
        }

        mDrawableRect.set(mBorderInnerRect);
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

        Path bitmapPath = new Path();
        bitmapPath.addRoundRect(mDrawableRect, mCornerInnerRadii, Path.Direction.CW);
        canvas.drawPath(bitmapPath, mBitmapPaint);

        drawBorder(canvas);
    }

    private void drawBorder(@NonNull Canvas canvas) {
        Path path = new Path();
        for (Side side : Side.values()) {
            path.rewind();
            mBorderPaint.setColor(mBorderColors[side.index]);
            calcBorderSegmentPath(path, side);
            canvas.drawPath(path, mBorderPaint);
        }
    }

    private void calcBorderSegmentPath(Path path, Side side) {
        Corner corner2 = Corner.ALL[side.index];
        Corner corner1 = corner2.prev();
        float splitAngle1 = getSplitAngleForCorner(corner1);
        float splitAngle2 = getSplitAngleForCorner(corner2);
        float normalAngle = 270f + corner1.index * 90f;
        RectF oval = new RectF();
        calcOvalRect(oval, mBorderOuterRect, mCornerRadii, corner1);
        path.arcTo(oval, normalAngle - 90f + splitAngle1, 90f - splitAngle1);
        calcOvalRect(oval, mBorderOuterRect, mCornerRadii, corner2);
        path.arcTo(oval, normalAngle, splitAngle2);
        calcOvalRect(oval, mBorderInnerRect, mCornerInnerRadii, corner2);
        path.arcTo(oval, normalAngle + splitAngle2, -splitAngle2);
        calcOvalRect(oval, mBorderInnerRect, mCornerInnerRadii, corner1);
        path.arcTo(oval, normalAngle, -(90f - splitAngle1));
        path.close();
    }

    private float getSplitAngleForCorner(Corner corner) {
        return (float) Math.toDegrees(Math.atan2(mBorderSizes[corner.index], mBorderSizes[corner.next().index]));
    }

    private void calcOvalRect(RectF out, RectF rect, float[] radii, Corner corner) {
        switch (corner) {
            case TOP_LEFT:
                out.set(rect.left, rect.top, rect.left + 2f * radii[0], rect.top + 2f * radii[1]);
                break;
            case TOP_RIGHT:
                out.set(rect.right - 2f * radii[2], rect.top, rect.right, rect.top + 2f * radii[3]);
                break;
            case BOTTOM_RIGHT:
                out.set(rect.right - 2f * radii[4], rect.bottom - 2f * radii[5], rect.right, rect.bottom);
                break;
            case BOTTOM_LEFT:
                out.set(rect.left, rect.bottom - 2f * radii[7], rect.left + 2f * radii[6], rect.bottom);
                break;
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

    public void setCornerRadius(Corner corner, float radius) {
        mCornerRadii[2 * corner.index] = radius;
        mCornerRadii[2 * corner.index + 1] = radius;
        switch (corner) {
            case TOP_LEFT:
                mCornerInnerRadii[2 * corner.index] = Math.max(radius - mBorderSizes[0], 0f);
                mCornerInnerRadii[2 * corner.index + 1] = Math.max(radius - mBorderSizes[1], 0f);
                break;
            case TOP_RIGHT:
                mCornerInnerRadii[2 * corner.index] = Math.max(radius - mBorderSizes[2], 0f);
                mCornerInnerRadii[2 * corner.index + 1] = Math.max(radius - mBorderSizes[1], 0f);
                break;
            case BOTTOM_RIGHT:
                mCornerInnerRadii[2 * corner.index] = Math.max(radius - mBorderSizes[2], 0f);
                mCornerInnerRadii[2 * corner.index + 1] = Math.max(radius - mBorderSizes[3], 0f);
                break;
            case BOTTOM_LEFT:
                mCornerInnerRadii[2 * corner.index] = Math.max(radius - mBorderSizes[0], 0f);
                mCornerInnerRadii[2 * corner.index + 1] = Math.max(radius - mBorderSizes[3], 0f);
                break;
        }
    }

    public void setBorderWidth(int side, float width) {
        mBorderSizes[side] = width;
    }

    public void setBorderColor(Side side, @ColorInt int color) {
        mBorderColors[side.index] = color;
    }

    public void setColor(int color) {
        mColor = color;
        invalidateSelf();
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