package com.mingchu.factory.presenter.account;

import com.mingchu.common.factory.presenter.BaseContract;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 登录
 */

public interface LoginContract {

    interface View extends BaseContract.View<Presenter>{
        //注册成功
        void loginSuccess();

    }


    interface Presenter extends BaseContract.Presenter{

        //发起注册
        void login(String phone,String password);

        //检查手机号是否正确
        boolean checkMobile(String phone);


    }
}
