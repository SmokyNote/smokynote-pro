package com.smokynote.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import com.smokynote.R;

/**
 * Indicator view.
 * Display given level by cutting <code>src</code> drawable.
 * <code>background</code> drawable is always visible.
 *
 * @author Maksim Zakharov
 * @author $Author$ (current maintainer)
 * @since 1.0
 */
public class ImageIndicatorView extends View {

    public static final int MAX_LEVEL = 10000;

    private Drawable mBackground;
    private Drawable mSrc;
    private int mLevel;

    private boolean mChanged = true;

    public ImageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        prepareGraphic(context, attrs, defStyle);
    }

    protected void prepareGraphic(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageIndicatorView, defStyle, 0);

        final Drawable background = a.getDrawable(R.styleable.ImageIndicatorView_android_background);
        final Drawable src = a.getDrawable(R.styleable.ImageIndicatorView_android_src);

        a.recycle();

        initDrawableResource(src);
        initDrawableResource(background);

        mSrc = new ClipDrawable(src, Gravity.BOTTOM, ClipDrawable.VERTICAL);
        mBackground = background;
    }

    private void initDrawableResource(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            ((BitmapDrawable) drawable).setAntiAlias(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int chosenWidth = chooseDimension(widthMeasureSpec);
        int chosenHeight = chooseDimension(heightMeasureSpec);

        int measuredDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(measuredDimension, measuredDimension);
    }

    private int chooseDimension(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        return chooseDimension(mode, size);
    }

    private int chooseDimension(int mode, int size) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                return size;
            default: // MeasureSpec.UNSPECIFIED
                return getPreferredSize();
        }
    }

    protected int getPreferredSize() {
        return 180;
    }

    /**
     * Set level to display.
     * Value must be in range 0 .. {@link #MAX_LEVEL}.
     * @see android.graphics.drawable.Drawable#setLevel(int)
     *
     * @param level level to display
     */
    public void setLevel(int level) {
        mLevel = level;

        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable background = mBackground;
        final int w = background.getIntrinsicWidth();
        final int h = background.getIntrinsicHeight();

        float scale = Math.min((float) availableWidth / (float) w,
                (float) availableHeight / (float) h);

        final Drawable src = mSrc;
        src.setLevel(mLevel);

        if (changed) {
            final int hw = w / 2;
            final int hh = h / 2;
            background.setBounds(x - hw, y - hh, x + hw, y + hh);
            src.setBounds(x - hw, y - hh, x + hw, y + hh);
        }

        canvas.save();
        canvas.scale(scale, scale, x, y);

        background.draw(canvas);
        src.draw(canvas);

        canvas.restore();
    }
}
