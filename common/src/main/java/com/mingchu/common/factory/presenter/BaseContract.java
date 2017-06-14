package com.mingchu.common.factory.presenter;

import android.support.annotation.StringRes;

import com.mingchu.common.widget.recycler.RecyclerAdapter;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 基本MVP模式中基本契约
 */

public interface BaseContract<T> {

    interface View<T extends Presenter> {

        //显示字符串错误
        void showError(@StringRes int str);

        //显示进度条
        void showLoading();

        void setPresenter(T presenter);

    }


    interface Presenter {

        //公共的开始的方法
        void start();

        //共用的销毁触发方法
        void destroy();

    }


    /**
     * 基本的列表view的一个职责
     * @param <T>
     */
    interface RecyclerView<T extends Presenter,ViewModel> extends View<T>{

        //自己拿到整个适配器  然后自己自主的刷新
        RecyclerAdapter<ViewModel> getRecyclerViewAadpter();

        //当适配器更改的时候触发
        void onAdapterDataChanged();
    }
}
