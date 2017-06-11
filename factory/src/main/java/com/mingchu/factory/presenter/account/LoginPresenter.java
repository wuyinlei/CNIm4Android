package com.mingchu.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.mingchu.common.Common;
import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.R;
import com.mingchu.factory.data.helper.AccountHelper;
import com.mingchu.factory.model.api.account.LoginModel;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * @function 登录的逻辑实现
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            //尝试传递pushid
            LoginModel loginModel = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(loginModel, this);
        }

    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }


    @Override
    public void onDataLoaded(User data) {
        //当网络请求成功得到了
        //告知界面  登录成功
        final LoginContract.View view = getView();
        if (view == null)
            return;
        //此事从网络回来的数据  并不能保证处于主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvaiable(@StringRes final int res) {
        //注册失败返回的错误信息
        final LoginContract.View view = getView();
        if (view == null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(res);
            }
        });
    }
}
