package com.mingchu.factory.presenter.search;

import com.mingchu.common.factory.presenter.BasePresenter;

/**
 * Created by wuyinlei on 2017/6/11.
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter {


    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
