package com.mingchu.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.mingchu.common.Common;
import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.R;
import com.mingchu.factory.data.helper.AccountHelper;
import com.mingchu.factory.model.api.account.RegisterModel;
import com.mingchu.factory.model.db.User;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;


/**
 * Created by wuyinlei on 2017/6/10.
 * <p>
 * 注册Presenter实现类
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }


    @Override
    public void register(String phone, String name, String password) {
        start();

        RegisterContract.View view = getView();
        //校验
        if (!checkMobile(phone)) {
            //提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //进行网络请求
            RegisterModel model = new RegisterModel(phone, password, name);
            //并设置回调接口
            AccountHelper.register(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        //手机号不能为空   并且满足某种格式
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(User data) {
        //当网络请求成功得到了注册号的用户信息回来
        //告知界面  注册成功
        final RegisterContract.View view = getView();
        if (view == null)
            return;
        //此事从网络回来的数据  并不能保证处于主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvaiable(@StringRes final int res) {
        //注册失败返回的错误信息
        final RegisterContract.View view = getView();
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
