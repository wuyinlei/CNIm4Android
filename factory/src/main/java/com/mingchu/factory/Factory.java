package com.mingchu.factory;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.StringRes;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mingchu.common.app.Application;
import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.data.group.GroupCenter;
import com.mingchu.factory.data.group.GroupDispatcher;
import com.mingchu.factory.data.message.MessageCenter;
import com.mingchu.factory.data.message.MessageDispatcher;
import com.mingchu.factory.data.user.UserCenter;
import com.mingchu.factory.data.user.UserDispatcher;
import com.mingchu.factory.model.api.PushModel;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.card.GroupMemberCard;
import com.mingchu.factory.model.card.MessageCard;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.utils.DBFlowExclusionStrategy;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
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
                .setExclusionStrategies(new DBFlowExclusionStrategy())  //
                .create();
    }

    public static Application app() {
        return Application.getInstance();
    }


    /**
     * Factory中的初始化
     */
    public static void setUp() {

        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)  //数据库初始化的时候打开数据库
                .build());

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
        Account.setToLogin();  //暂时的退出登录逻辑
    }


    /**
     * 处理推送来的消息
     *
     * @param message 消息
     */
    public static void dispathPushMessage(String message) {
        if (!Account.isLogin())
            return;
        PushModel model = PushModel.decode(message);
        if (model == null)
            return;

        Log.d("Factory", model.toString());

        //对推送集合进行遍历
        for (PushModel.Entity entity : model.getEntities()) {
            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    instance.logout();
                    //退出情况下直接方法  并且不可继续
                    return;

                //普通消息
                case PushModel.ENTITY_TYPE_MESSAGE: {
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }

                //添加一个朋友
                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }

                //添加群
                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatcher(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    Type type = new TypeToken<List<GroupMemberCard>>(){}.getType();
                    List<GroupMemberCard> cards = getGson().fromJson(entity.content, type);
                    //把数据集合丢到数据中心处理
                    getGroupCenter().dispatcher(cards.toArray(new GroupMemberCard[0]));
                    break;
                }

                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:
                    //成员退出的推送

                    break;


            }
        }

    }

    /**
     * 获取一个消息中心的实现类
     *
     * @return 消息中心的规范接口
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

    /**
     * 获取一个用户中心的实现类
     *
     * @return 规范的接口
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }


    /**
     * 获取一个群处理中心的实现类
     *
     * @return 规范的接口
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }


}
