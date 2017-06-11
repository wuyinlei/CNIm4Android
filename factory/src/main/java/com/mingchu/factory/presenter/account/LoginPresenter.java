package com.mingchu.factory.presenter.account;

import com.mingchu.common.factory.presenter.BasePresenter;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * @function 登录的逻辑实现
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter{

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }

    @Override
    public boolean checkMobile(String phone) {
        return false;
    }


}
