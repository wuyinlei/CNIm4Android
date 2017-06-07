package com.mingchu.common.app;

import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public class Application extends android.app.Application {
    protected static Application instance;
    private static File cacheDir;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    public static void showToast(final String msg) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }

}
