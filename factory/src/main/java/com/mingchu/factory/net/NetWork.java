package com.mingchu.factory.net;

import android.text.TextUtils;
import android.widget.TextView;

import com.mingchu.common.Common;
import com.mingchu.factory.Factory;
import com.mingchu.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 用于网络请求 封装
 */

public class NetWork {


    private static NetWork instance;
    private static Retrofit mRetrofit;
//    private Retrofit mRetrofit;

    static {
        instance = new NetWork();
    }

    private NetWork(){

    }

    public static NetWork getInstance() {
        return instance;
    }

    //构建一个Retrofit
    public static Retrofit getRetrofit() {

        if (mRetrofit != null)
            return mRetrofit;
        //得到一个OkHttp的client
        OkHttpClient client = new OkHttpClient.Builder()
                //给所有的请求添加一个拦截器  注入我们需要的参数
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拿到我们的原始请求
                        Request originRequest = chain.request();
                        //重新进行builder
                        Request.Builder builder = originRequest.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())){
                            builder.addHeader("token",Account.getToken());
                        }

                        builder.addHeader("Content-Type","application/json");
                        Request newRequest = builder.build();
                        //返回
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        //设置电脑连接

        mRetrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client)  //设置client
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))  //gson解析
                .build();
        return mRetrofit;  //构建retrofit
    }

    public static RemoteService remote(){
        return NetWork.getRetrofit().create(RemoteService.class);
    }

}
