package com.mingchu.factory.presenter.search;

import com.mingchu.common.factory.presenter.BasePresenter;

/**
 * Created by wuyinlei on 2017/6/11.
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter{


    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
