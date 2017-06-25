package com.mingchu.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingchu.common.factory.presenter.BaseContract;

/**
 * Created by wuyinlei on 2017/6/10.
 * <p>
 * Presenter的fragment
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends BaseFragment implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
    }

    /**
     * 初始化presenter
     *
     * @return 返回一个
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(@StringRes int str) {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null)
            mPlaceHolderView.triggerLoading();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //View中额赋值Presenter
        this.mPresenter = presenter;
    }


}
