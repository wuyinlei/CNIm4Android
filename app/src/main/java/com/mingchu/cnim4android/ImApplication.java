package com.mingchu.cnim4android;

import com.igexin.sdk.PushManager;
import com.mingchu.common.app.Application;
import com.mingchu.factory.Factory;

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
}
