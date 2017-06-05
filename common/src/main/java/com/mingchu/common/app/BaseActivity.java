package com.mingchu.common.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by wuyinlei on 2017/6/4.
 * <p>
 * 公共的activity
 */

public abstract class BaseActivity extends AppCompatActivity {


    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用额初始化窗口
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            //设置界面layoutId
            setContentView(getContentLayoutId());

            initView();

            initData();
        } else {
            finish();
        }

    }

    /**
     * 初始化窗口
     */
    protected  void initWindows(){

    }

    /**
     * 初始化窗口
     * @param bundle  数据
     * @return  如果初始化成功  返回true
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 当前控件id
     *
     * @return layoutId
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
    }



    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时  finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        //首先得到当前activity下的所有fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragmentList != null && fragmentList.size() > 0){
            //如果不为空
            for (Fragment fragment : fragmentList) {
                //判断是否是我们自己能够处理的fragment
                if (fragment instanceof com.mingchu.common.app.BaseFragment){
                    //判断是否拦截了返回按钮
                    if (((BaseFragment) fragment).onBackPressed()){
                        //如果有  直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
