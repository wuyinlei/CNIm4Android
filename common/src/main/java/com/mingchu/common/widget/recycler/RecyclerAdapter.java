package com.mingchu.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingchu.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wuyinlei on 2017/6/4.
 * <p>
 * 封装的RecyclerViewAdapter
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements View.OnLongClickListener, View.OnClickListener, AdapterCallBack<T> {

    private final List<T> mDataLists;


    private AdapterItemClickListener<T> mAdapterItemClickListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterItemClickListener listener) {
        this(new ArrayList<T>(), listener);
    }

    public RecyclerAdapter(List<T> dataLists, AdapterItemClickListener<T> adapterItemClickListener) {
        mDataLists = dataLists;
        mAdapterItemClickListener = adapterItemClickListener;
    }


    /**
     * 创建一个ViewHolder
     *
     * @param parent   父布局
     * @param viewType 布局类型  这里可以约定  约定为布局xml的id
     * @return ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //把xml的id为viewType的转换为一个root view
        View root = inflater.inflate(viewType, parent, false);
        //必须实现的方法  得到一个ViewHolder
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);
        //设置VIew的tag  进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        //设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //进行节目注解绑定
        holder.mUnbinder = ButterKnife.bind(holder, root);
        holder.mCallBack = this;
        return holder;
    }

    /**
     * 复写默认的布局类型
     *
     * @param position 坐标
     * @return 类型：其实复写后返回的都是xml文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataLists.get(position));
    }

    @Override
    public void update(T data, ViewHolder<T> holder) {
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            mDataLists.remove(pos);
            mDataLists.add(pos, data);
            notifyItemChanged(pos);
        }
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数据
     * @return xml的文件的id  用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, T data);


    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 就是xml的id
     * @return ViewHolder
     */
    protected abstract ViewHolder<T> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T data = mDataLists.get(position);  //得到需要绑定的数据
        //出发holder的绑定的方法
        holder.bind(data);  //绑定方法 要不然没有数据进行触发
    }

    @Override
    public int getItemCount() {
        return mDataLists.size();
    }

    /**
     * 返回整个集合
     *
     * @return 数据集合
     */
    public List<T> getItems() {
        return mDataLists;
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (mAdapterItemClickListener != null) {
            int pos = holder.getAdapterPosition();
            mAdapterItemClickListener.onLongItemClick(holder, mDataLists.get(pos));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (mAdapterItemClickListener != null) {
            int pos = holder.getAdapterPosition();
            mAdapterItemClickListener.onItemClick(holder, mDataLists.get(pos));
        }
    }

    /**
     * 自定义的监听器
     *
     * @param <T> 泛型
     */
    public interface AdapterItemClickListener<T> {
        //当cell点击的时候触发
        void onItemClick(ViewHolder holder, T data);

        //当cell长按点击的时候触发
        void onLongItemClick(ViewHolder holder, T data);
    }

    /**
     * 设置点击监听器
     *
     * @param adapterItemClickListener 自定义的适配器监听器
     */
    public void setAdapterItemClickListener(AdapterItemClickListener<T> adapterItemClickListener) {
        mAdapterItemClickListener = adapterItemClickListener;
    }


    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        private Unbinder mUnbinder;

        private AdapterCallBack<T> mCallBack;

        protected T mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的出发
         *
         * @param data 绑定的数据
         */
        void bind(T data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当出发绑定数据的时候的回调 必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(T data);

        /**
         * 更新 自己主动刷新
         *
         * @param data 数据
         */
        public void updateData(T data) {
            if (this.mCallBack != null) {
                mCallBack.update(data, this);
            }
        }

    }

    /**
     * 插入一条数据并通知刷新
     *
     * @param data data
     */
    public void add(T data) {
        mDataLists.add(data);
        notifyItemInserted(mDataLists.size() - 1);
    }

    /**
     * 插入一堆数据 并通知这段数据刷新
     *
     * @param datas datas
     */
    @SafeVarargs
    public final void add(T... datas) {
        if (datas != null && datas.length > 0) {
            int startPos = mDataLists.size();
            Collections.addAll(mDataLists, datas);
            notifyItemRangeChanged(startPos, datas.length);
        }
    }

    /**
     * 插入一堆数据 并通知这段数据刷新
     *
     * @param datas datas
     */
    public void add(Collection<T> datas) {
        if (datas != null && datas.size() > 0) {
            int startPos = mDataLists.size();
            mDataLists.addAll(datas);
            notifyItemRangeChanged(startPos, datas.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataLists.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了额情况
     *
     * @param datas 一个新的集合
     */
    public void replace(Collection<T> datas) {
        mDataLists.clear();
        if (datas != null && datas.size() > 0) {
            mDataLists.addAll(datas);
        }
        notifyDataSetChanged();
    }

}
