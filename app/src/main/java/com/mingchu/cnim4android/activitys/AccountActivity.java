package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.account.AccountTrigger;
import com.mingchu.cnim4android.fragment.account.LoginFragment;
import com.mingchu.cnim4android.fragment.account.RegisterFragement;
import com.mingchu.cnim4android.fragment.account.UpdateUserInfoFragment;
import com.mingchu.common.app.BaseActivity;
import com.mingchu.common.app.BaseSwipeBackActivity;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;


public class AccountActivity extends BaseSwipeBackActivity implements AccountTrigger {


    private Fragment mCurrentFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.iv_bg)
    ImageView mIvBg;

    /**
     * 账户Activity显示的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
        super.initView();

        mLoginFragment = mCurrentFragment = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, mCurrentFragment)
                .commit();

        Glide.with(this)
                .load(R.mipmap.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView,GlideDrawable>(mIvBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable drawable = resource.getCurrent();
                        //使用适配包进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(),R.color.colorPrimary),
                                PorterDuff.Mode.SCREEN);


                        this.view.setImageDrawable(drawable);
                    }
                });

    }


    @Override
    public void triggerView() {
        Fragment fragment = null;
        if (mCurrentFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                //默认情况下 为空  要新建一个fragment
                mRegisterFragment = new RegisterFragement();
            } else {
                fragment = mRegisterFragment;
            }
        } else {
            fragment = mLoginFragment;  //不需要判空 默认情况下  已经赋值了
        }

        mCurrentFragment = fragment;
        //切换显示
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lay_container, mCurrentFragment)
                .commit();

    }
}
