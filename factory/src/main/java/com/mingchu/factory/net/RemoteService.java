package com.mingchu.factory.net;

import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.account.AccountRspModel;
import com.mingchu.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wuyinlei on 2017/6/10.
 *
 * @function 网络请求的所有接口
 */

public interface RemoteService {

    /**
     * 网络请求的一个注册接口
     *
     * @param model 传入的model
     * @return 返回的是AccountRspModel
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);
}
