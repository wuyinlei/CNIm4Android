package com.mingchu.cnim4android.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Created by wuyinlei on 2017/6/6.
 *
 * @function 解决对Fragment的调度与重用问题
 * 达到最优的Fragment切换
 */

public class NavFraHelper<T> {

    private final SparseArray<Tab<T>> tabs = new SparseArray<>(); //所有的tab集合

    //用于初始化的必须的参数
    private final int containerId;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> mListener;

    private Tab<T> currentTab;  //当前选中的的tab

    public NavFraHelper(Context context,
                        int containerId,
                        FragmentManager fragmentManager,
                        OnTabChangedListener<T> listener) {
        this.containerId = containerId;
        this.context = context;
        this.fragmentManager = fragmentManager;
        mListener = listener;
    }

    /**
     * 添加对应的tab
     *
     * @param menuId 对应的menuId
     * @param tab    添加的tab
     */
    public NavFraHelper<T> addTab(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前显示的tab
     *
     * @return 当前显示的tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }


    /**
     * 执行点击菜单的操作
     *
     * @param menuId 菜单的id
     * @return 是否能够处理这个点击
     */
    public boolean performClikcMenu(int menuId) {
        //取集合中的tab  如果有就处理  没有就不处理
        Tab<T> tTab = tabs.get(menuId);
        if (tTab != null) {
            doSelectTab(tTab);
            return true;
        }
        return false;
    }

    /**
     * 进行操作
     *
     * @param tab Tab
     */
    private void doSelectTab(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //如果当前的Tab就是点击的Tab  那么我们不做处理 或者刷新处理
                nofityReselect(tab);
                return;
            }
        }
        //赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 切换tab
     *
     * @param newTab 新的需要切换的tab
     * @param oldTab 当前的tab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面中移除  但是还在Fragment的缓存控件中
                ft.detach(oldTab.fragment);
            }
        }

        if (newTab != null) {
            if (newTab.fragment == null) {
                //首次新建
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                //缓存起来
                newTab.fragment = fragment;
                //提交到FragmentManager
                ft.add(containerId, fragment, newTab.clx.getName());
            } else {  //如果不为空  就从FragmentManager的缓存空间中重新加载到界面中
                ft.attach(newTab.fragment);
            }
        }
        //提交事务
        ft.commit();
        //通知回调
        notifyTabSelect(newTab, oldTab);
    }


    /**
     * 回调给我们的监听器  在外部可以拿到
     *
     * @param newTab newTab  新的tab
     * @param oldTab oldTab  旧的tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (mListener != null) {
            mListener.onTabChanged(newTab, oldTab);
        }
    }

    private void nofityReselect(Tab<T> tab) {
        //二次点击的刷新操作
    }

    /**
     * 菜单 我们所有的Tab的基础属性
     *
     * @param <T> 泛型额外的参数
     */
    @SuppressWarnings("WeakerAccess")
    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        public Class<?> clx;
        public T extra;  //额外的字段  用户自己设定需要
        Fragment fragment;  //内部缓存的对应的Fragment
    }

    /**
     * 定义事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
