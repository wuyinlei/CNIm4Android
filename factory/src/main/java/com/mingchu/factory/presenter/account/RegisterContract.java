package com.mingchu.factory.presenter.account;

import android.support.annotation.StringRes;

import com.mingchu.common.factory.presenter.BaseContract;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 注册
 */

public interface RegisterContract {

   public interface View extends BaseContract.View<Presenter>{
        //注册成功
        void registerSuccess();

        //显示字符串错误
        void showError(@StringRes int str);

    }


   public interface Presenter extends BaseContract.Presenter{

        //发起注册
        void register(String phone,String name,String password);

        //检查手机号是否正确
        boolean checkMobile(String phone);


    }
}
