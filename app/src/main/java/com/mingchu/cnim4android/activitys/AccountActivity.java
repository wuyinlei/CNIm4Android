package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.account.UpdateUserInfoFragment;
import com.mingchu.common.app.BaseActivity;
import com.mingchu.common.app.BaseSwipeBackActivity;

import butterknife.BindView;


public class AccountActivity extends BaseSwipeBackActivity {

    @BindView(R.id.title_bar)
    Toolbar mToolbar;

    /**
     * 账户Activity显示的入口
     * @param context  上下文
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
        super.initView();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, new UpdateUserInfoFragment())
                .commit();

        setSupportActionBar(mToolbar);
        //设置是否有返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}
