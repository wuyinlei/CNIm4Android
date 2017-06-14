package com.mingchu.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/14.
 * <p>
 * DiffUiDataCallback
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {

    private List<T> mOldDatas;
    private List<T> mNewDatas;

    public DiffUiDataCallback(List<T> oldDatas, List<T> newDatas) {
        mOldDatas = oldDatas;
        mNewDatas = newDatas;
    }

    @Override
    public int getOldListSize() {
        //旧的数据大小
        return mOldDatas.size();
    }

    @Override
    public int getNewListSize() {
        //新的数据大小
        return mNewDatas.size();
    }

    //表明两个类就是同一个东西   比如id相等的User
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldDatas.get(oldItemPosition);
        T beanNew = mNewDatas.get(newItemPosition);

        return beanNew.isSame(beanOld);
    }

    //在经过相等判断后  进一步判断是否有数据更改
    //比如 同一个用户的两个不同实例  其中的name字段不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        //
        T beanOld = mOldDatas.get(oldItemPosition);
        T beanNew = mNewDatas.get(newItemPosition);

        return beanNew.isUiContentSame(beanOld);
    }

    //进行比较的数据类型
    //泛型的目的  就是你和一个你这个类型的数据进行比较
    public interface UiDataDiffer<T> {
        //传递一个旧的数据给你   问你是否和你标识的是同一个数据
        boolean isSame(T old);

        //你和旧的数据对比  内容是否相同
        boolean isUiContentSame(T data);
    }
}
