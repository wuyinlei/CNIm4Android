package com.mingchu.factory.data.helper;

import android.text.TextUtils;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.Factory;
import com.mingchu.factory.R;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.account.AccountRspModel;
import com.mingchu.factory.model.api.account.LoginModel;
import com.mingchu.factory.model.api.account.RegisterModel;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.net.NetWork;
import com.mingchu.factory.net.RemoteService;
import com.mingchu.factory.persistence.Account;

import retrofit2.Call;
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
//        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        //调用Retrofit对我们的网络请求接口做代理
        Call<RspModel<AccountRspModel>> rspModelCall = NetWork.remote().accountRegister(model);
        //进行异步的请求
        rspModelCall.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登录的接口  异步
     *
     * @param model    登录的model
     * @param callback 成功和失败的接口回送
     */
    public static void login(LoginModel model, final DataSource.Callback<User> callback) {
//        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        //调用Retrofit对我们的网络请求接口做代理
        Call<RspModel<AccountRspModel>> rspModelCall = NetWork.remote().accountLogin(model);
        //进行异步的请求
        rspModelCall.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 绑定的接口
     *
     * @param callback 成功和失败的接口回送
     */
    public static void bindPush(DataSource.Callback<User> callback) {

        String pushId = Account.getPushId();
        if (!TextUtils.isEmpty(pushId)) {
            //不为空
            NetWork.remote().accountBind(pushId)
                    .enqueue(new AccountRspCallback(callback));

        } else {

        }

    }


    /**
     * 请求封装
     */
    private static class AccountRspCallback implements retrofit2.Callback<RspModel<AccountRspModel>> {

        private DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            //从返回中得到我们的全局model  内部是使用的Gson解析
            RspModel<AccountRspModel> body = response.body();

            if (body != null && body.success()) {
                //拿到实体
                AccountRspModel accountRspModel = body.getResult();
                final User user = accountRspModel.getUser();
                //进行数据库写入和绑定
                if (user != null) {
                    DbHelper.save(User.class,user);
//                    user.save();  //第一种 直接保存

//                            FlowManager.getModelAdapter(User.class)
//                                    .save(user);    第二种保存方法

                    //第三中存储  放到事务中
//                            DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
//                            definition.beginTransactionAsync(new ITransaction() {
//                                @Override
//                                public void execute(DatabaseWrapper databaseWrapper) {
//                                    FlowManager.getModelAdapter(User.class)
//                                            .save(user);
//                                }
//                            }).build().execute();

                    Account.login(accountRspModel);


                    if (accountRspModel.isBind()) {  //是绑定状态
                        if (callback != null)
                            callback.onDataLoaded(user);
                        Account.setBind(true);
                    }
                } else {
                    // TODO: 2017/6/10 绑定设备
                    bindPush(callback);
//                        callback.onDataLoaded(accountRspModel.getUser());
                }
            } else {
//                   todo 对返回的body中的失败的Code进行解析 解析到对应的String资源上面
                //错误解析
                Factory.decodeRspCode(body, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback != null)
                callback.onDataNotAvaiable(R.string.data_network_error);
        }
    }

}
