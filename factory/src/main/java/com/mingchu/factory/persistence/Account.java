package com.mingchu.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mingchu.factory.Factory;
import com.mingchu.factory.model.api.account.AccountRspModel;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.model.db.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * @function
 */

public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USER_ACCOUNT = "KEY_USER_ACCOUNT";


    //设备的推送Id
    private static String pushId;

    private static boolean isBind;  //设备id是否已经绑定到了服务器
    //登录状态的token
    private static String token;
    //登录状态的userId
    private static String userId;
    //登录状态的account
    private static String account;


    /**
     * 获取推送id
     *
     * @return pushId
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 存储到xml文件  持久化
     */
    private static void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        //存储
        sp.edit().putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ACCOUNT, account)
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    /**
     * 进行数据加载
     *
     * @param context 上下文
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_USER_ACCOUNT, "");
    }

    /**
     * 设置pustId
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        save(Factory.app());  //存储设备id
    }

    /**
     * 返回当前账号是否登录
     *
     * @return true  false
     */
    public static boolean isLogin() {

        return !TextUtils.isEmpty(userId) &&
                !TextUtils.isEmpty(token);
    }

    /**
     * 重置UserId和Token  用于当Token单点登录的时候重新登录使用  在请求的时候先判断一下是否登录
     * 如果是未登录  让用户重新登录然后在请求数据
     */
    public static void setToLogin() {
        userId = "";
        token = "";
        save(Factory.app());
    }

    /**
     * 是否已经完善了用户信息
     *
     * @return false   true
     */
    public static boolean isCompleteUserInfo() {
        return isLogin();
    }


    /**
     * 是否已经绑定到了服务器
     *
     * @return false  true
     */
    public static boolean isBind() {
        return isBind;
    }

    /**
     * 设置绑定状态
     *
     * @param isBind 绑定状态
     */
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        save(Factory.app());
    }

    /**
     * 保存我自己的信息到XML持久化
     *
     * @param model 我自己
     */
    public static void login(AccountRspModel model) {
        //存储用户的token   用户id  方便从数据库中查询
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        save(Factory.app());
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user
     */
    public static User getUser() {
        //如果为null  返回一个new User  其次从数据库中查询
        return TextUtils.isEmpty(userId) ? new User() : SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId)).querySingle();
    }

    /**
     * 返回用户id
     *
     * @return UserId
     */
    public static String getUserId() {
        return getUser().getId();
    }

    /**
     * 获取到token
     *
     * @return token
     */
    public static String getToken() {
        return token;
    }


}
