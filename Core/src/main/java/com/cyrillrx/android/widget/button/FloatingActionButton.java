package com.cyrillrx.android.widget.button;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.cyrillrx.android.R;
import com.cyrillrx.android.widget.scroll.ObservableScrollView;
import com.cyrillrx.android.widget.scroll.OnScrollChangedListener;
import com.cyrillrx.android.widget.scroll.ScrollDirectionListener;
import com.cyrillrx.android.widget.scroll.ScrollViewThresholdListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Cyril Leroux
 *         Created on 17/01/2015
 */
public class FloatingActionButton extends ImageButton {

    private static final int TRANSLATE_DURATION_MILLIS = 200;
    private static final int DEFAULT_COLOR_RIPPLE = Color.WHITE;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_NORMAL, TYPE_MINI})
    public @interface FabType {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MINI = 1;

    private boolean mVisible;

    private int mDefaultColorNormal;
    private int mDefaultColorPressed;

    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;
    private boolean mShadow;
    private int mType;

    private int mShadowSize;

    private int mScrollThreshold;

    private boolean mMarginsSet;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attributeSet) {
        mDefaultColorNormal = getColor(android.R.color.holo_blue_light);
        mDefaultColorPressed = getColor(android.R.color.holo_blue_dark);

        mColorNormal = mDefaultColorNormal;
        mColorPressed = mDefaultColorPressed;
        mColorRipple = DEFAULT_COLOR_RIPPLE;

        mVisible = true;

        mType = TYPE_NORMAL;
        mShadow = true;
        mScrollThreshold = getResources().getDimensionPixelOffset(R.dimen.fab_scroll_threshold);
        mShadowSize = getDimension(R.dimen.fab_shadow_size);
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }
        updateBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getDimension(mType == TYPE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
        if (mShadow && !hasLollipopApi()) {
            size += mShadowSize * 2;
            setMarginsWithoutShadow();
        }
        setMeasuredDimension(size, size);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {

        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton, 0, 0);

        if (attr == null) {
            return;
        }

        try {
            mColorNormal = attr.getColor(R.styleable.FloatingActionButton_colorNormal, mDefaultColorNormal);
            mColorPressed = attr.getColor(R.styleable.FloatingActionButton_colorPressed, mDefaultColorPressed);
            mColorRipple = attr.getColor(R.styleable.FloatingActionButton_colorRipple, DEFAULT_COLOR_RIPPLE);
            mShadow = attr.getBoolean(R.styleable.FloatingActionButton_shadow, true);
            mType = attr.getInt(R.styleable.FloatingActionButton_type, TYPE_NORMAL);
        } finally {
            attr.recycle();
        }
    }

    private void updateBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(mColorPressed));
        drawable.addState(new int[]{}, createDrawable(mColorNormal));
        setBackgroundCompat(drawable);
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        if (mShadow && !hasLollipopApi()) {
            Drawable shadowDrawable = getResources().getDrawable(mType == TYPE_NORMAL ? R.drawable.shadow : R.drawable.shadow_mini);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, shapeDrawable});
            layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);
            return layerDrawable;
        } else {
            return shapeDrawable;
        }
    }

    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    private int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private void setMarginsWithoutShadow() {
        if (!mMarginsSet) {
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = layoutParams.leftMargin - mShadowSize;
                int topMargin = layoutParams.topMargin - mShadowSize;
                int rightMargin = layoutParams.rightMargin - mShadowSize;
                int bottomMargin = layoutParams.bottomMargin - mShadowSize;
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                requestLayout();
                mMarginsSet = true;
            }
        }
    }

    private void setBackgroundCompat(Drawable drawable) {
        if (hasLollipopApi()) {
            setBackgroundLollipop(drawable);
        } else if (hasJellyBeanApi()) {
            setBackground(drawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setBackgroundLollipop(Drawable drawable) {
        float elevation;
        if (mShadow) {
            elevation = getElevation() > 0.0f ? getElevation() : getDimension(R.dimen.fab_elevation);
        } else {
            elevation = 0.0f;
        }
        setElevation(elevation);
        RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{{}}, new int[]{mColorRipple}), drawable, null);
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int size = getDimension(mType == TYPE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
                outline.setOval(0, 0, size, size);
            }
        });
        setClipToOutline(true);
        setBackground(rippleDrawable);
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    public void setColorNormal(int color) {
        if (color != mColorNormal) {
            mColorNormal = color;
            updateBackground();
        }
    }

    public void setColorNormalResId(@ColorRes int colorResId) {
        setColorNormal(getColor(colorResId));
    }

    public int getColorNormal() {
        return mColorNormal;
    }

    public void setColorPressed(int color) {
        if (color != mColorPressed) {
            mColorPressed = color;
            updateBackground();
        }
    }

    public void setColorPressedResId(@ColorRes int colorResId) {
        setColorPressed(getColor(colorResId));
    }

    public int getColorPressed() {
        return mColorPressed;
    }

    public void setColorRipple(int color) {
        if (color != mColorRipple) {
            mColorRipple = color;
            updateBackground();
        }
    }

    public void setColorRippleResId(@ColorRes int colorResId) {
        setColorRipple(getColor(colorResId));
    }

    public int getColorRipple() {
        return mColorRipple;
    }

    public void setShadow(boolean shadow) {
        if (shadow != mShadow) {
            mShadow = shadow;
            updateBackground();
        }
    }

    public boolean hasShadow() { return mShadow; }

    public void setType(@FabType int type) {
        if (type != mType) {
            mType = type;
            updateBackground();
        }
    }

    @FabType
    public int getType() { return mType; }

    public boolean isVisible() { return mVisible; }

    public void show() { show(true); }

    public void hide() { hide(true); }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                setTranslationY(translationY);
            }

            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
            if (!hasHoneycombApi()) {
                setClickable(visible);
            }
        }
    }

    /**
     * Attaches the floating action button to the given scrollView.<br />
     * <b>Note : This will PRESERVE any OnScrollChangedListener set to the scrollView BEFORE the call of this function.</b>
     *
     * @param scrollView The scrollView on which to attach the button.
     */
    public void attachToScrollView(@NonNull ObservableScrollView scrollView) {
        attachToScrollView(scrollView, scrollView.getOnScrollChangedListener(), null);
    }

    /**
     * Attaches the floating action button to the given scrollView.<br />
     * <b>Note : This will REPLACE any OnScrollChangedListener set to the scrollView by the one in parameter .</b>
     *
     * @param scrollView              The scrollView on which to attach the button.
     * @param onScrollChangedListener A listener to apply to the ObservableScrollView.
     * @param scrollDirectionListener A listener to apply to the ObservableScrollView.
     */
    public void attachToScrollView(@NonNull ObservableScrollView scrollView, OnScrollChangedListener onScrollChangedListener, ScrollDirectionListener scrollDirectionListener) {
        ScrollViewEventDecorator scrollViewEventDecorator = new ScrollViewEventDecorator();
        scrollViewEventDecorator.setThreshold(mScrollThreshold);
        scrollViewEventDecorator.setOnScrollChangedListener(onScrollChangedListener);
        scrollViewEventDecorator.setScrollDirectionListener(scrollDirectionListener);
        scrollView.setOnScrollChangedListener(scrollViewEventDecorator);
    }

    private boolean hasLollipopApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean hasJellyBeanApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private class ScrollViewEventDecorator extends ScrollViewThresholdListener {

        private OnScrollChangedListener mScrollChangedListener;
        private ScrollDirectionListener mScrollDirectionListener;

        private void setOnScrollChangedListener(OnScrollChangedListener scrollChangedListener) {
            mScrollChangedListener = scrollChangedListener;
        }

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        @Override
        public void onScrollChanged(ScrollView who, int x, int y, int oldX, int oldY) {
            super.onScrollChanged(who, x, y, oldX, oldY);
            if (mScrollChangedListener != null) {
                mScrollChangedListener.onScrollChanged(who, x, y, oldX, oldY);
            }
        }

        @Override
        public void onScrollDown() {
            show();

            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            hide();

            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }
    }
}