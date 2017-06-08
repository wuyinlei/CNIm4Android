package com.mingchu.factory;

import android.support.annotation.NonNull;

import com.mingchu.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wuyinlei on 2017/6/8.
 * <p>
 * 上传工厂类
 */

public class Factory {

    private final Executor defaultExecutor;

    private static final Factory instance;

    static {
        instance = new Factory();
    }

    public Factory() {
        defaultExecutor = Executors.newFixedThreadPool(4);//新建一个线程池
    }

    public static Application app() {
        return Application.getInstance();
    }

    /**
     * 异步执行的方法
     *
     * @param runnable runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单利  拿到线程池
        instance.defaultExecutor.execute(runnable);  //执行runnable
    }


}
