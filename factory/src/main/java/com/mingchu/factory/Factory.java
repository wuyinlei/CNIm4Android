package com.mingchu.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mingchu.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wuyinlei on 2017/6/8.
 * <p>
 * 上传工厂类
 */

public class Factory {

    //全局的线程池
    private final Executor defaultExecutor;

    private static final Factory instance;

    //全局的gson
    private final Gson mGson;

    static {
        instance = new Factory();
    }

    public Factory() {
        defaultExecutor = Executors.newFixedThreadPool(4);//新建一个线程池
        mGson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") //设置时间格式
                // TODO: 2017/6/10   设置一个过滤器   数据库级别的model不进行Gson转换
//                .setExclusionStrategies()
                .create();
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

    /**
     * 返回全局的Gson
     * @return  Gson
     */
    public static Gson getGson(){
        return instance.mGson;
    }


}
