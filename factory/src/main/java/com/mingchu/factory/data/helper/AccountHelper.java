package com.mingchu.factory.data.helper;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.R;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.account.AccountRspModel;
import com.mingchu.factory.model.api.account.RegisterModel;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.net.NetWork;
import com.mingchu.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 账号所有请求帮助类
 */

public class AccountHelper {

    public interface Callback {
        void onDataLoaded();
    }


    /**
     * 注册的接口  异步
     *
     * @param model    注册的model
     * @param callback 成功和失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        //调用Retrofit对我们的网络请求接口做代理
        Call<RspModel<AccountRspModel>> rspModelCall = service.accountRegister(model);
        //进行异步的请求
        rspModelCall.enqueue(new retrofit2.Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                //从返回中得到我们的全局model  内部是使用的Gson解析
                RspModel<AccountRspModel> body = response.body();
                if (body != null && body.success()) {
                    //拿到实体
                    AccountRspModel accountRspModel = body.getResult();
                    if (accountRspModel.isBind()) {  //是绑定状态
                        User user = accountRspModel.getUser();
                        //进行数据库写入和绑定
                        if (user != null)
                            callback.onDataLoaded(user);
                    } else {
                        // TODO: 2017/6/10 绑定设备
//                        bindPush(callback);
                        callback.onDataLoaded(accountRspModel.getUser());
                    }
                } else {
//                   todo 对返回的body中的失败的Code进行解析 解析到对应的String资源上面
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                callback.onDataNotAvaiable(R.string.data_network_error);
            }
        });
    }

    public static void bindPush(DataSource.Callback<User> callback){

    }

}
