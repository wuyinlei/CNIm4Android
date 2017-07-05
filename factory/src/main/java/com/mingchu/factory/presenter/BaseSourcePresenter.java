package com.mingchu.factory.presenter;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.data.DbDataSource;
import com.mingchu.common.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/10.
 * <p>
 * 基础的仓库源Presenter定义
 */

/**
 *
 * @param <Data>
 * @param <ViewModel>
 * @param <Source>
 * @param <View>
 */
public abstract class BaseSourcePresenter<Data, ViewModel, Source extends DbDataSource<Data>, View extends
        BaseContract.RecyclerView> extends
        BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SuccessCallback<List<Data>> {

    private Source mSource;

    public BaseSourcePresenter(View view, Source source) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null)
            mSource.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mSource != null) {
            mSource.dispose();
            mSource = null;
        }
    }
}
