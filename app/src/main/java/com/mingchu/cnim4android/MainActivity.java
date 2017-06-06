package com.mingchu.cnim4android;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.mingchu.cnim4android.utils.NavFraHelper;
import com.mingchu.common.app.BaseActivity;
import com.mingchu.common.widget.custom.PortraitView;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


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

    @BindView(R.id.iv_arrow)
    ImageView mIvArRow;


    NavFraHelper mNavFraHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }



    @Override
    protected void initView() {
        super.initView();

//        mNavFraHelper = new NavFraHelper();

        Glide.with(this).load(R.mipmap.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

        //要这样写  要不然找不到控件  我擦嘞
        mNavigationView.setOnNavigationItemSelectedListener(this);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotain);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        mIvArRow.setAnimation(operatingAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             mIvArRow.setAnimation(null);
            }
        },4000);

    }


    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.iv_search)
    void onSearchMenuClick() {
        Toast.makeText(this, "搜索按钮点击了", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.iv_portrait)
    void onPortraitClick() {
        Toast.makeText(this, "头像点了", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt_action)
    void onActionClick() {
        Toast.makeText(this, "浮动按钮点击了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件流到自定义的工具类端
        mTxtTitle.setText(item.getTitle());
        return true;
    }
}
