package com.mingchu.cnim4android.fragment.message;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.PersonalActivity;
import com.mingchu.common.app.ToolbarActivity;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.presenter.message.ChatContract;
import com.mingchu.factory.presenter.message.ChatUserPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wuyinlei
 * @function 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView {

    @BindView(R.id.iv_portrait)
    PortraitView mPortraitView;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingtoolbarlayout;

    private MenuItem mInfoMenuItem;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        MenuItem menuItem = mInfoMenuItem;
        if (mInfoMenuItem == null)
            return;

        if (verticalOffset == 0) {
            //完全展开
            mPortraitView.setVisibility(View.VISIBLE);
            mPortraitView.setScaleX(1);
            mPortraitView.setScaleY(1);
            mPortraitView.setAlpha(1);

            //隐藏menu菜单
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            //拖动的时候
            verticalOffset = Math.abs(verticalOffset);

            final int totalScroll = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScroll) {
                mPortraitView.setVisibility(View.INVISIBLE);
                mPortraitView.setScaleX(0);
                mPortraitView.setScaleY(0);
                mPortraitView.setAlpha(0);

                //显示菜单
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {
                //中间状态
                float progress = 1 - verticalOffset / (float) totalScroll;
                mPortraitView.setVisibility(View.VISIBLE);
                mPortraitView.setScaleX(progress);
                mPortraitView.setScaleY(progress);
                mPortraitView.setAlpha(progress);

                //和头像相反
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha((int) (255 - 255 * progress));

            }

        }

    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        Glide.with(this)
                .load(R.mipmap.default_bannar_chat)
                .centerCrop()
                .into(new ViewTarget<CollapsingToolbarLayout, GlideDrawable>(mCollapsingtoolbarlayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setContentScrim(resource.getCurrent());
                    }
                });

    }


    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = mToolbar;

        mToolbar.inflateMenu(R.menu.chat_user);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onPortraitClick();
                }
                return false;
            }
        });

        mInfoMenuItem = mToolbar.getMenu().findItem(R.id.action_person);


    }

    @OnClick(R.id.iv_portrait)
    void onPortraitClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }


    @Override
    public void onInit(User user) {
        //对聊天信息需要用到的数据的初始化操作
        mPortraitView.setup(Glide.with(getActivity()), user);
        mCollapsingtoolbarlayout.setTitle(user.getName());


    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this, mReceiverId);
    }
}
