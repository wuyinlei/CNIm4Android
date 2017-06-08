package com.mingchu.cnim4android;

import com.mingchu.cnim4android.activitys.MainActivity;
import com.mingchu.cnim4android.fragment.assist.PermissionsFragment;
import com.mingchu.common.app.BaseActivity;

public class LaunchActivity extends BaseActivity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionsFragment.haveAllPerms(this, getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        } else {

        }
    }
}
