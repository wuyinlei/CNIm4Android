package com.mingchu.factory.presenter.search;

import android.support.annotation.StringRes;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.data.user.UserCenter;
import com.mingchu.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * Created by wuyinlei on 2017/6/11.
 * <p>
 * 搜索的Presenter
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {


    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        //如果有上一次的请求  也没有取消  则调用取消操作
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        searchCall = UserHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> data) {
        final SearchContract.UserView view = getView();
        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(data);
                }
            });
        }
    }

    @Override
    public void onDataNotAvaiable(@StringRes final int res) {
        final SearchContract.UserView view = getView();
        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(res);
                }
            });
        }
    }
}
