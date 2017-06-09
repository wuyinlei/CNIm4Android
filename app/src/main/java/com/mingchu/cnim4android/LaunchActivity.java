package com.mingchu.cnim4android;

import com.mingchu.cnim4android.activitys.MainActivity;
import com.mingchu.cnim4android.fragment.assist.PermissionsFragment;
import com.mingchu.common.app.BaseActivity;

public class LaunchActivity extends BaseActivity {

    private boolean isFirst = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
            if (PermissionsFragment.haveAllPerms(this, getSupportFragmentManager())) {
                MainActivity.show(this);
                finish();
            } else {

            }
        } else if (PermissionsFragment.haveAllPerms(this, getSupportFragmentManager())) {
//            PermissionsFragment fragment = new PermissionsFragment();
//            fragment.dismiss();

            MainActivity.show(this);
            finish();
        }
    }
}
