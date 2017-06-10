package com.mingchu.factory.net;

import com.mingchu.common.Common;
import com.mingchu.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 用于网络请求 封装
 */

public class NetWork {

    //构建一个Retrofit
    public static Retrofit getRetrofit() {

        //得到一个OkHttp的client
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit.Builder builder = new Retrofit.Builder();
        //设置电脑连接

        return builder.baseUrl(Common.Constance.API_URL)
                .client(client)  //设置client
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))  //gson解析
                .build();  //构建retrofit
    }

}
