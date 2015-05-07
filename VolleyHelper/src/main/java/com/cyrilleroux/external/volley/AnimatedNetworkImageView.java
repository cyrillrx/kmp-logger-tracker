package com.cyrillrx.external.volley;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Overrides Volley NetworkImageView to add a fade in animation when the image is loaded.
 * <p/>
 * Created on 10/09/2014.
 */
public class AnimatedNetworkImageView extends NetworkImageView {

    private static final int DEFAULT_ANIM_DURATION = 400;
    private boolean mShouldAnimate;

    public AnimatedNetworkImageView(Context context) {
        super(context);
    }

    public AnimatedNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (mShouldAnimate) {
            ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(DEFAULT_ANIM_DURATION).start();
        }
    }

    @Override
    public void setImageUrl(String url, ImageLoader imageLoader) {
        mShouldAnimate = !imageLoader.isCached(url, 0, 0);
        super.setImageUrl(url, imageLoader);
    }
}
