package com.mingchu.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingchu.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wuyinlei on 2017/6/4.
 * <p>
 * 基类Fragment</>
 */

public abstract class BaseFragment extends Fragment {

    private View mRoot;
    private Unbinder mUnbinder;

    protected PlaceHolderView mPlaceHolderView;
    //是否第一次初始化数据
    protected boolean mIsFirstInitData = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = mRoot;
        if (mRoot == null) {
            int layoutId = getContentLayoutId();
            view = inflater.inflate(layoutId, container, false);
            mRoot = view;
            initView(view);
        } else {
            if (mRoot.getParent() != null) {
                //把当前root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot = view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    /**
     * 初始化窗口
     *
     * @param bundle 数据
     * @return 如果初始化成功  返回true
     */
    protected void initArgs(Bundle bundle) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当view创建完成后初始化数据
        if (mIsFirstInitData) {
            mIsFirstInitData = false;  //触发一次后就不在触发
            onFirstInit();
        }
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsFirstInitData = true;
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
    protected void initView(View view) {
        mUnbinder = ButterKnife.bind(this, view);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 当首次初始化的时候
     */
    protected void onFirstInit() {

    }

    /**
     * 返回按键初始化时候调用
     *
     * @return 返回true表示我已经处理返回逻辑   Activity不需要finish
     * 返回false表示我没有处理返回逻辑  Activity自己处理返回逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    protected boolean isSaveView() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (!isSaveView() && mRoot != null) {
            if (mUnbinder != null)
                mUnbinder.unbind();
            mRoot = null;
        }
    }

    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        mPlaceHolderView = placeHolderView;
    }
}
