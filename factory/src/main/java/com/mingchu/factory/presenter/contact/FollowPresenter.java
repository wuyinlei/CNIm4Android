package com.mingchu.factory.presenter.contact;

import android.support.annotation.StringRes;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by wuyinlei on 2017/6/12.
 * <p>
 * FollowPresenter
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter ,DataSource.Callback<UserCard>{

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();

        UserHelper.follow(id,this);
    }

    @Override
    public void onDataLoaded(final UserCard data) {
        final FollowContract.View view = getView();
        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSuccess(data);
                }
            });
        }
    }

    @Override
    public void onDataNotAvaiable(@StringRes final int res) {
        final FollowContract.View view = getView();
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
