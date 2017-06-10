package com.mingchu.cnim4android.fragment.account;


import android.content.Context;

import com.mingchu.cnim4android.R;
import com.mingchu.common.app.BaseFragment;

/**
 * 登录界面
 */
public class LoginFragment extends BaseFragment {


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
