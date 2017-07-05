package com.mingchu.factory.utils;

import android.support.v7.widget.RecyclerView;

/**
 * 更新的callback  用于获取到插入到的位置  用于最后的recyclerview的移动到指定位置
 */

public class ListUpdateCallback implements android.support.v7.util.ListUpdateCallback {
    private RecyclerView.Adapter adapter;
    private int insertedPosition = -1;

    public ListUpdateCallback(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        adapter.notifyItemRangeInserted(position, count);
        insertedPosition = position + count - 1;
    }

    @Override
    public void onRemoved(int position, int count) {
        adapter.notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        adapter.notifyItemRangeChanged(position, count, payload);
    }

    public int getInsertPosition() {
        return insertedPosition;
    }
}
