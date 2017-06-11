package com.mingchu.cnim4android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.mingchu.cnim4android.activitys.AccountActivity;
import com.mingchu.cnim4android.activitys.MainActivity;
import com.mingchu.cnim4android.fragment.assist.PermissionsFragment;
import com.mingchu.factory.persistence.Account;

import net.qiujuer.genius.res.Resource;

public class LaunchActivity extends AppCompatActivity {

    private ColorDrawable mColorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = getResources().getColor(R.color.colorPrimary);
        //创建Drawable
        ColorDrawable colorDrawable = new ColorDrawable(color);
        root.setBackground(colorDrawable);
        mColorDrawable = colorDrawable;

        init();
    }

    private void init() {
        //开始动画   进入到动画的50%时候等待PushId获取
        start(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushServiceId();
            }
        });
    }

    /**
     * 等待个推框架对我们的PushId设置好值
     */
    private void waitPushServiceId() {

        if(Account.isLogin()){
            //登录
            if (Account.isBind()){
                //登录且已经绑定
                skip();
                return;
            }

        } else {
            //没有登录
            //如果拿到了PushId  没有登录的情况下 不能绑定PushID
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();  //进行跳转
            }
        }

        //没有拿到PushId   循环等待
        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitPushServiceId();
                    }
                },500);
    }

    /**
     * 真正的界面替换
     */
    private void reallySkip() {
            if (Account.isLogin()) {
                //进入到主界面  已经登录
                MainActivity.show(LaunchActivity.this);
            } else {
                //没有登录  跳转登录界面
                AccountActivity.show(LaunchActivity.this);
            }
            finish();
    }

    private void skip() {
        start(1f, new Runnable() {
            @Override
            public void run() {
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reallySkip();
                    }
                }, 500);
            }
        });
    }

    private void start(float progress, final Runnable endCallbacl) {
        //获取一个最终的颜色
        int white = Resource.Color.WHITE;
        //运算当前进度的颜色
        int endColor = (int) new ArgbEvaluator().evaluate(progress, mColorDrawable.getColor(), white);
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, new ArgbEvaluator(), endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mColorDrawable.getColor(), endColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallbacl.run();
            }
        });
        valueAnimator.start();
    }


    private final Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "animValue") {
        @Override
        public Object get(LaunchActivity object) {
            return object.mColorDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mColorDrawable.setColor((Integer) value);
        }
    };
}
