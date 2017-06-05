package com.mingchu.common.widget.recycler;

/**
 * Created by wuyinlei on 2017/6/4.
 *
 * Adapter接口
 */

public interface AdapterCallBack<T> {

    /**
     * 进行更新操作
     * @param date
     * @param holder
     */
    void update(T date,RecyclerAdapter.ViewHolder<T> holder);

}
