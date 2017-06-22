package com.mingchu.common.factory.data;

import android.support.annotation.StringRes;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 数据源接口
 */

public interface DataSource {

    /**
     * 同时包括了成功和失败的接口
     *
     * @param <T> 任意类型
     */
    public interface Callback<T> extends SuccessCallback<T>, FailedCallback<T> {

    }

    /**
     * 只关注成功的接口
     *
     * @param <T>
     */
    public interface SuccessCallback<T> {
        //数据加载成功  网络请求成功
        void onDataLoaded(T data);
    }

    /**
     * 只关注失败的接口
     *
     * @param <T>
     */
    public interface FailedCallback<T> {
        //数据加载失败  网络请求失败
        void onDataNotAvaiable(@StringRes int res);
    }

    void dispose();

}
