package com.mingchu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mingchu.common.R;

/**
 * Created by wuyinlei on 2017/6/7.
 *
 * @function 正方形控件
 */

public class SquareLayout extends FrameLayout {

    private int mBaseDirection = 1;

    public SquareLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, null, 0, 0);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout, defStyleAttr, defStyleRes);
        mBaseDirection = array.getInt(R.styleable.SquareLayout_comAccordTo, 3);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //高宽给父类  传递的测量值都是宽度  那么就可以形成基于宽度的正方形控件
        if (mBaseDirection == 1) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else if (mBaseDirection == 2) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            if (heightSize == 0) {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                return;
            }

            if (widthSize == 0) {
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                return;
            }

            if (widthSize > heightSize)
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            else
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}
