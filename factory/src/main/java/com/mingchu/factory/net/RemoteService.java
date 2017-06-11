package com.mingchu.factory.net;

import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.account.AccountRspModel;
import com.mingchu.factory.model.api.account.LoginModel;
import com.mingchu.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    /**
     * 登录接口
     *
     * @param model 传入的model
     * @return 返回的是AccountRspModel
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定PushId
     *
     * @param pushId 传入的pushId
     * @return 绑定PushId
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);
}
