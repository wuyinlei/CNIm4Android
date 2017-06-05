package com.mingchu.cnim4android;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.mingchu.common.app.BaseActivity;
import com.mingchu.common.widget.custom.PortraitView;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.appbar)
    AppBarLayout mLayAppBar;

    @BindView(R.id.fragment_container)
    FrameLayout mFrameLayoutContainer;

    @BindView(R.id.navigation_bottom)
    BottomNavigationView mNavigationView;

    @BindView(R.id.bt_action)
    FloatActionButton mActionButton;


    @BindView(R.id.iv_search)
    ImageView mIvSearch;


    @BindView(R.id.iv_portrait)
    PortraitView mPortraitView;


    @BindView(R.id.txt_title)
    TextView mTxtTitle;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).load(R.mipmap.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View,GlideDrawable>(mLayAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.iv_search)
    void onSearchMenuClick(){

    }


    @OnClick(R.id.iv_portrait)
    void onPortraitClick(){

    }

    @OnClick(R.id.bt_action)
    void onActionClick(){

    }
}
