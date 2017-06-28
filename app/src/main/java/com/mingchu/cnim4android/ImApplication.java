package com.mingchu.cnim4android;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.mingchu.cnim4android.activitys.AccountActivity;
import com.mingchu.common.app.Application;
import com.mingchu.factory.Factory;
import com.mingchu.factory.persistence.Account;

/**
 * Created by wuyinlei on 2017/6/8.
 *
 * @function 全局Application
 */

public class ImApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //持久化数据初始化
        Factory.setUp();


        //推送SDK初始化
        PushManager.getInstance().initialize(this);


    }

    @Override
    protected void showAccount(Context context) {
        super.showAccount(context);

        AccountActivity.show(context);
    }
}
