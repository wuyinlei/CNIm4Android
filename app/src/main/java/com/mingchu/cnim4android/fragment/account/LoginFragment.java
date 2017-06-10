package com.mingchu.cnim4android.fragment.account;


import android.content.Context;
import android.widget.EditText;

import com.mingchu.cnim4android.R;
import com.mingchu.common.app.BaseFragment;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;

/**
 * 登录界面
 */
public class LoginFragment extends BaseFragment {


    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    public LoginFragment() {
        // Required empty public constructor
    }

    private AccountTrigger mAccountTrigger;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到AccoutActivity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAccountTrigger.triggerView();
    }
}
