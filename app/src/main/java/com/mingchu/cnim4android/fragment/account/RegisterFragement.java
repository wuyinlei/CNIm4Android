package com.mingchu.cnim4android.fragment.account;


import android.content.Context;

import com.mingchu.cnim4android.R;
import com.mingchu.common.app.BaseFragment;

/**
 * @function 注册界面
 */
public class RegisterFragement extends BaseFragment {


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

}
