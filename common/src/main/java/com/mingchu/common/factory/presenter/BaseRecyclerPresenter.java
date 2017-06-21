package com.mingchu.common.factory.presenter;

import android.support.v7.util.DiffUtil;

import com.mingchu.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/21.
 *
 * @function 对RecyclerView进行的一个简单的Presenter封装
 */

public class BaseRecyclerPresenter<ViewModel, View extends BaseContract.RecyclerView> extends BasePresenter<View> {

    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    protected void refreshData(final List<ViewModel> dataLists) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null)
                    return;

                //基本的更新数据并刷新界面
                RecyclerAdapter<ViewModel> adapter = view.getRecyclerViewAadpter();
                adapter.replace(dataLists);
                view.onAdapterDataChanged();

            }
        });
    }

    /**
     * 刷新数据到界面中
     *
     * @param diffResult 差异的结果集
     * @param datalists  具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewModel> datalists) {

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //这里是主线程
                refreshDataOnUiThread(diffResult, datalists);
            }
        });
    }

    /**
     * 刷新界面操作   该操作一定在主线程中进行
     *
     * @param diffResult 差异的结果集
     * @param datalists  具体的新数据
     */
    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewModel> datalists) {
        View view = getView();
        if (view == null)
            return;

        //基本的更新数据并刷新界面刷新
        RecyclerAdapter<ViewModel> adapter = view.getRecyclerViewAadpter();
        //改变数据集合并不通知
        adapter.getItems().clear();
        adapter.getItems().addAll(datalists);
        //通知界面刷新占位布局
        view.onAdapterDataChanged();
        //进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }


}
