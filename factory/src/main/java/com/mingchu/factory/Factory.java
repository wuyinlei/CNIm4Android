package com.mingchu.factory;

import android.graphics.PorterDuff;
import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mingchu.common.app.Application;
import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.persistence.Account;

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
     * Factory中的初始化
     */
    public static void setUp(){
        Account.load(app());   //持久化的数据 进行初始化
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
     *
     * @return Gson
     */
    public static Gson getGson() {
        return instance.mGson;
    }

    /**
     * 进行错误code解析
     * 把网络返回的code值进行统一的规划并返回一个String资源
     *
     * @param model          Resmodel
     * @param failedCallback DataSource.FailedCallback
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback failedCallback) {
        if (model == null)
            return;

        //进行code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, failedCallback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, failedCallback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, failedCallback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, failedCallback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, failedCallback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, failedCallback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, failedCallback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, failedCallback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, failedCallback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, failedCallback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();// TODO: 2017/6/11 这个地方退出登录
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, failedCallback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, failedCallback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, failedCallback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, failedCallback);
                break;
        }
    }

    public static void decodeRspCode(@StringRes final int str, DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvaiable(str);
    }

    /**
     * 收到账户退出的消息 要进行账户退出
     */
    private void logout() {

    }

    /**
     * 处理推送来的消息
     * @param message  消息
     */
    public static void dispathPushMessage(String message){

    }


}
