package com.mingchu.cnim4android.fragment.message;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MessageActivity;
import com.mingchu.common.app.BaseFragment;

import butterknife.BindView;

/**
 * Created by wuyinlei on 2017/6/23.
 *
 * @function 聊天的基本
 */

public abstract class ChatFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {

    protected String mReceiverId;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mBarLayout;


    EditText mEtContent;


    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);

        initToolbar();
        initAppbar();
    }

    protected void initToolbar(){
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 给界面的appbar设置一个监听  得到关闭和打开的时候的进度
     */
    private void initAppbar(){
        mBarLayout.addOnOffsetChangedListener(this);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }



}
