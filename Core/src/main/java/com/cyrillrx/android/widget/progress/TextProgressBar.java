package com.cyrillrx.android.widget.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.cyrillrx.android.R;

/**
 * @author Cyril Leroux
 *         Created on 02/01/15
 */
public class TextProgressBar extends ProgressBar {

    private static final String DEFAULT_TEXT = "";
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final float DEFAULT_TEXT_SIZE = 16f;

    private Paint mTextPaint;
    private Rect mTextBounds;

    private String mText;
    private int mTextColor;
    private float mTextSize;

    public TextProgressBar(Context context) {
        super(context);
        init();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextProgressBar,
                0, 0);

        try {
            mText = a.getString(R.styleable.TextProgressBar_text);
            mTextColor = a.getColor(R.styleable.TextProgressBar_textColor, DEFAULT_COLOR);
            mTextSize = a.getDimension(R.styleable.TextProgressBar_textSize, DEFAULT_TEXT_SIZE);
        } finally {
            a.recycle();
        }

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mTextBounds = new Rect();
    }

    private void init() {
        mText = DEFAULT_TEXT;
        mTextColor = DEFAULT_COLOR;
        mTextSize = DEFAULT_TEXT_SIZE;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mTextBounds = new Rect();
    }

    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mText == null || mText.isEmpty()) {
            return;
        }

        // Calculate text bounds
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);

        // Calculate center position
        int x = getWidth() / 2 - mTextBounds.centerX();
        int y = getHeight() / 2 - mTextBounds.centerY();

        // Drawing the text
        canvas.drawText(mText, x, y, mTextPaint);
    }

    public String getText() { return mText; }

    public synchronized void setText(String text) { mText = text; }

    public int getTextColor() { return mTextColor; }

    public synchronized void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
    }

    public float getTextSize() { return mTextSize; }

    public synchronized void setTextSize(float textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
    }
}
