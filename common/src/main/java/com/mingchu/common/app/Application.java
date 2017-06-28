package com.mingchu.common.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @function
 */
public class Application extends android.app.Application {
    protected static Application instance;
    private static File cacheDir;
    private  List<BaseActivity> mActivities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        instance.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivities.add((BaseActivity) activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivities.remove(activity);
            }
        });
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

    /**
     * 获取声音文件的本地缓存地址
     * @param isTmp  是否是缓存文件
     * @return  缓存文件地址
     */
    public static File getAudioFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        File path = new File(getCacheDirFile(), isTmp ? "tmp.mp3" : SystemClock.currentThreadTimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前app缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * 获取到裁剪后的图片存储地址
     *
     * @return 裁剪后的图片地址File
     */
    public static File getPortraitFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        //创建所有对应的文件夹
        dir.mkdirs();
        //删除旧的一些缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        File path = new File(dir, SystemClock.currentThreadTimeMillis() + ".jpg");
        return path.getAbsoluteFile();  //返回一个当前时间戳的文件地址
    }


    public   void finishAll(Context context){
        for (BaseActivity activity : mActivities) {
            activity.finish();
        }

        showAccount(context);
    }

    protected void showAccount(Context context) {

    }


}
