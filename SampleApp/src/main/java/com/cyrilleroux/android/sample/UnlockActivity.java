package com.cyrilleroux.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cyrilleroux.android.core.toolbox.CompletionListener;
import com.cyrilleroux.android.core.toolbox.CountDownTimer;

/**
 * Created on 02/10/2014.
 */
public class UnlockActivity extends Activity implements View.OnTouchListener, CompletionListener {

    private static final int DURATION_MS = 1000;
    private static final long STEP_MS = 10;

    private CountDownProgress mTimer1;
    private CountDownProgress mTimer2;
    private CountDownProgress mTimer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unlock);

        ProgressBar lock1 = (ProgressBar) findViewById(R.id.pb_lock_1);
        ProgressBar lock2 = (ProgressBar) findViewById(R.id.pb_lock_2);
        ProgressBar lock3 = (ProgressBar) findViewById(R.id.pb_lock_3);

        mTimer1 = new CountDownProgress(DURATION_MS, STEP_MS, lock1, this);
        mTimer2 = new CountDownProgress(DURATION_MS, STEP_MS, lock2, this);
        mTimer3 = new CountDownProgress(DURATION_MS, STEP_MS, lock3, this);

        findViewById(android.R.id.background).setOnTouchListener(this);
        lock1.setOnTouchListener(this);
        lock2.setOnTouchListener(this);
        lock3.setOnTouchListener(this);
    }

    @Override
    public void onCompleted() {
        if (mTimer1.isComplete() && mTimer2.isComplete() && mTimer3.isComplete()) {
            Toast.makeText(getApplicationContext(), "All done !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                return dispatchStartFilling(v.getId(), event);

            case MotionEvent.ACTION_UP:
                return dispatchStartEmptying(v.getId(), event);
        }
        return false;
    }

    private boolean dispatchStartFilling(int viewId, MotionEvent event) {

        int pointerId = event.getPointerId(event.getActionIndex());

        switch (viewId) {
            case R.id.pb_lock_1:
                mTimer1.startFilling(pointerId);
                return true;

            case R.id.pb_lock_2:
                mTimer2.startFilling(pointerId);
                return true;

            case R.id.pb_lock_3:
                mTimer3.startFilling(pointerId);
                return true;

            default:
                return true;
        }
    }

    private boolean dispatchStartEmptying(int viewId, MotionEvent event) {

        int pointerId = event.getPointerId(event.getActionIndex());

        switch (viewId) {
            case R.id.pb_lock_1:
                mTimer1.startEmptying(pointerId);
                return true;

            case R.id.pb_lock_2:
                mTimer2.startEmptying(pointerId);
                return true;

            case R.id.pb_lock_3:
                mTimer3.startEmptying(pointerId);
                return true;

            default:
        }
        return true;
    }

    public static class CountDownProgress extends CountDownTimer {

        private ProgressBar mProgress;
        private final int mMax;
        private boolean mClockWise;
        private boolean mStarted;
        private int mPointerId;
        private boolean mCompleted;
        private CompletionListener mCompletionListener;

        public CountDownProgress(int millisInFuture, long countDownInterval, ProgressBar progressBar, CompletionListener completionListener) {
            super(millisInFuture, countDownInterval);
            mCompletionListener = completionListener;
            mProgress = progressBar;
            mMax = millisInFuture;
            mProgress.setMax(millisInFuture);
            mClockWise = true;
            mStarted = false;
            mPointerId = -1;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (mClockWise ? mMax - millisUntilFinished : millisUntilFinished);
            mProgress.setProgress(progress);
        }

        @Override
        public void onFinish() {
            if (mClockWise) {
                mProgress.setProgress(mMax);
                mCompleted = true;
                mCompletionListener.onCompleted();
            } else {
                mProgress.setProgress(0);
                mStarted = false;
            }
        }

        public void startFilling(int pointerId) {

            // if not started, bind pointer and start filling
            if (!mStarted) {
                mPointerId = pointerId;
                mClockWise = true;
                mMillisInFuture = mMax - mProgress.getProgress();
                start();

            } else if (!mClockWise) {
                // if restarting, update pointer and change direction
                mPointerId = pointerId;
                mClockWise = true;
            }
        }

        public void startEmptying(int pointerId) {

            // If the pointer up is not the one bond to the progress,
            if (pointerId != mPointerId) {return;}

            int progress = mProgress.getProgress();
            cancel();
            mCompleted = false;
            mClockWise = false;
            mMillisInFuture = progress;
            start();
        }

        public boolean isComplete() {
            return mCompleted;
        }
    }
}