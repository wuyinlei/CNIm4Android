package com.mingchu.factory.presenter.account;

import android.text.TextUtils;

import com.mingchu.common.Common;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.data.helper.AccountHelper;
import com.mingchu.factory.model.api.RegisterModel;

import java.util.regex.Pattern;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * 注册Presenter实现类
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }


    @Override
    public void register(String phone, String name, String password) {
        start();
        if (!checkMobile(phone)){
            //提示
        } else if (name.length() < 2){

        } else if (password.length() < 6){

        } else {
            //进行网络请求
            RegisterModel model = new RegisterModel(phone,password,name);
            AccountHelper.register(model);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        //手机号不能为空   并且满足某种格式
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE,phone);
    }
}
