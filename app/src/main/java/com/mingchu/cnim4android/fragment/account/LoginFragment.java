package com.mingchu.cnim4android.fragment.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MainActivity;
import com.mingchu.common.app.BaseFragment;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.factory.presenter.account.LoginContract;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录界面
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter>
        implements LoginContract.View {
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    private AccountTrigger mAccountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_submit)
    void submit() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.login(phone, password);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void changeStatus(boolean enable) {
        mSubmit.setEnabled(enable);
        mPhone.setEnabled(enable);
        mPassword.setEnabled(enable);
        if (enable) {
            mLoading.stop();
        } else {
            mLoading.start();
        }
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        changeStatus(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        changeStatus(false);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.txt_go_register)
    void showRegister() {
        mAccountTrigger.triggerView();
    }
}
