package com.mingchu.cnim4android.fragment.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.MainActivity;
import com.mingchu.common.app.PresenterFragment;
import com.mingchu.factory.presenter.account.RegisterContract;
import com.mingchu.factory.presenter.account.RegisterPresenter;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @function 注册界面
 */
public class RegisterFragement extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {


    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    private AccountTrigger mAccountTrigger;

    public RegisterFragement() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register_fragement;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到AccoutActivity的引用
        mAccountTrigger = (AccountTrigger) context;
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.txt_go_login)
    void showLogin() {
        mAccountTrigger.triggerView();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_submit)
    void submit() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String name = mName.getText().toString();
       mPresenter.register(phone, name, password);
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
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
    public void registerSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
//        mAccountTrigger.triggerView();
    }
}
