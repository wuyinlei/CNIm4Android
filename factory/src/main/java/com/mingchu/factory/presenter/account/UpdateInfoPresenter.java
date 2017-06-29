package com.mingchu.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.R;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.db.User;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by wuyinlei on 2017/6/29.
 */

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>implements UpdateInfoContract.Presenter ,DataSource.Callback<User> {


    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(String name, String photoPath, String desc, boolean isMan) {
        UpdateInfoContract.View view = getView();
        view.showLoading();

        if (TextUtils.isEmpty(photoPath) || TextUtils.isEmpty(desc))
            view.showError(R.string.data_account_update_invalid_parameter);
        else
            UserHelper.update(name, photoPath, desc, isMan ? 2 : 1, this);
    }

    @Override
    public void updatePhoto(String photoPath) {

    }


    @Override
    public void updateBase(String name, String desc) {

    }

    @Override
    public void onDataLoaded(User user) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                getView().updateSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvaiable(@StringRes final int res) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                getView().showError(res);
            }
        });
    }
}
