package com.mingchu.common.factory.presenter;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 基本的presenter
 */

public abstract class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {


    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /**
     * 设置一个View  子类可以复写完成
     *
     * @param view view
     */
    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /**
     * 给子类使用  获取到View的操作
     *
     * @return View
     */
    public final T getView() {
        return mView;
    }

    @Override
    public void start() {
        T view = mView;
        if (view != null) {
            view.showLoading();
        }

    }

    @Override
    public void destroy() {
        T view = mView;
        if (view != null) {
            //view销毁的时候  设置为null
            view.setPresenter(null);
            mView = null;
        }
    }
}
