package com.mingchu.common.app;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.mingchu.common.R;


public abstract class ToolbarActivity extends BaseSwipeBackActivity {
    protected Toolbar mToolbar;

    @Override
    protected void initView() {
        super.initView();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    protected void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(false);
        }
    }
}
