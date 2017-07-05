package com.mingchu.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mingchu.common.R;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.FloatActionButton;

public class AudioRecordView extends FrameLayout implements View.OnTouchListener {
    public static final int END_TYPE_CANCEL = 0;
    public static final int END_TYPE_NONE = 1;
    public static final int END_TYPE_PLAY = 2;
    public static final int END_TYPE_DELETE = 3;

    private static final float MIN_ALPHA = 0.4f;
    private final float[] mTouchPoint = new float[2];
    private final Rect mPlayLocation = new Rect();
    private final Rect mDeleteLocation = new Rect();
    private final Rect mRectLocation = new Rect();
    private FloatActionButton mRecordButton;
    private ImageView mPlayButton, mDeleteButton;
    private boolean mStart;
    private Callback mCallback;

    public AudioRecordView(@NonNull Context context) {
        super(context);
        init();
    }

    public AudioRecordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioRecordView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.lay_record_view, this);
        mRecordButton = (FloatActionButton) findViewById(R.id.btn_record);
        mPlayButton = (ImageView) findViewById(R.id.im_play);
        mDeleteButton = (ImageView) findViewById(R.id.im_delete);
        mRecordButton.setOnTouchListener(this);
        turnRecord();
    }

    public void setup(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mPlayLocation.left = mPlayButton.getLeft() - left;
        mPlayLocation.top = mPlayButton.getTop() - top;
        mPlayLocation.right = mPlayButton.getRight() - left;
        mPlayLocation.bottom = mPlayButton.getBottom() - top;

        mDeleteLocation.left = mDeleteButton.getLeft() - left;
        mDeleteLocation.top = mDeleteButton.getTop() - top;
        mDeleteLocation.right = mDeleteButton.getRight() - left;
        mDeleteLocation.bottom = mDeleteButton.getBottom() - top;

        mRectLocation.left = mRecordButton.getLeft() - left;
        mRectLocation.top = mRecordButton.getTop() - top;
        mRectLocation.right = mRecordButton.getRight() - left;
        mRectLocation.bottom = mRecordButton.getBottom() - top;
    }


    private void turnRecord() {
        if (mStart) {
            mPlayButton.animate()
                    .alpha(MIN_ALPHA)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(320)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
            mDeleteButton.animate()
                    .alpha(MIN_ALPHA)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(320)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
        } else {
            mPlayButton.animate()
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(260)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            mDeleteButton.animate()
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(260)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    public void setStatus(boolean isStart) {
        this.mStart = isStart;
        post(new Runnable() {
            @Override
            public void run() {
                turnRecord();
            }
        });
    }

    private void onStart() {
        Callback callback = mCallback;
        if (callback != null) {
            callback.onRequestRecordStart();
        }
    }

    private void onStop(boolean isCancel) {
        if (!mStart)
            return;

        mStart = false;
        turnRecord();

        Callback callback = mCallback;
        if (callback != null) {
            callback.onRecordEnd(isCancel ? END_TYPE_CANCEL :
                    (mActiveView == null ? END_TYPE_NONE : (
                            mActiveView == mPlayButton ? END_TYPE_PLAY : END_TYPE_DELETE)));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float pointX = event.getX();
                float pointY = event.getY();

                mTouchPoint[0] = pointX + mRectLocation.left;
                mTouchPoint[1] = pointY + mRectLocation.top;

                boolean inLeft = mTouchPoint[0] < mRectLocation.centerX();

                Rect rect = inLeft ? mPlayLocation : mDeleteLocation;
                double spaceLen = calculatePointDistance(mTouchPoint[0], mTouchPoint[1],
                        rect.centerX(), rect.centerY());

                refreshAlpha(inLeft, spaceLen);
                break;
            case MotionEvent.ACTION_DOWN:
                onStart();
                break;
            case MotionEvent.ACTION_CANCEL:
                onStop(true);
                break;
            case MotionEvent.ACTION_UP:
                onStop(false);
                break;
        }
        return false;
    }

    public interface Callback {
        void onRequestRecordStart();

        void onRecordEnd(int type);
    }

    private double calculatePointDistance(float px1, float py1, float px2, float py2) {
        double spaceX = Math.abs(px1 - px2);
        double spaceY = Math.abs(py1 - py2);
        return Math.sqrt(spaceX * spaceX + spaceY * spaceY);
    }

    private float mLastProgress;
    private View mActiveView;

    private void refreshAlpha(boolean inLeft, double spaceLen) {
        Rect rect = inLeft ? mPlayLocation : mDeleteLocation;
        double maxLen = calculatePointDistance(mRectLocation.centerX(), mRectLocation.centerY(),
                rect.centerX(), rect.centerY());


        float progress = Math.round(spaceLen / maxLen * 1000) / 1000f;
        if (mLastProgress == progress)
            return;
        mLastProgress = progress;
        Log.e("TAG", "mLastProgress:" + mLastProgress);
        progress = 1 - Math.max(0, Math.min(1, progress));

        float[] touchPoint = mTouchPoint;
        boolean overFlowIcon = rect.contains((int) touchPoint[0], (int) touchPoint[1]);

        ImageView activeView, noneView;
        if (inLeft) {
            activeView = mPlayButton;
            noneView = mDeleteButton;
        } else {
            activeView = mDeleteButton;
            noneView = mPlayButton;
        }

        activeView.setAlpha(MIN_ALPHA + (1 - MIN_ALPHA) * progress);
        int actionTintColor = overFlowIcon ? UiCompat.getColor(getResources(), R.color.colorAccent) :
                UiCompat.getColor(getResources(), R.color.textPrimary);
        DrawableCompat.setTint(activeView.getDrawable(), actionTintColor);
        DrawableCompat.setTint(activeView.getBackground(), actionTintColor);

        noneView.setAlpha(MIN_ALPHA);
        int noneTintColor = UiCompat.getColor(getResources(), R.color.textPrimary);
        DrawableCompat.setTint(noneView.getDrawable(), noneTintColor);
        DrawableCompat.setTint(noneView.getBackground(), noneTintColor);

        float scale = 1 + 0.2f * progress;
        activeView.setScaleX(scale);
        activeView.setScaleY(scale);

        mActiveView = overFlowIcon ? activeView : null;
    }
}
