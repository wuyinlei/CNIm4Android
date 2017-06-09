package com.mingchu.common.widget.recycler;

/**
 * Created by wuyinlei on 2017/6/4.
 * <p>
 * Adapter接口
 */

public interface AdapterCallBack<T> {

    /**
     * 进行更新操作
     *
     * @param date   当前数据类型
     * @param holder 当前RecyclerView的ViewHolder
     */
    void update(T date, RecyclerAdapter.ViewHolder<T> holder);

}
