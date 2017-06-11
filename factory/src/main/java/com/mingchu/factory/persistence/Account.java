package com.mingchu.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.mingchu.factory.Factory;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * @function
 */

public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";


    //设备的推送Id
    private static String pushId;

    private static boolean isBind;  //设备id是否已经绑定到了服务器

    /**
     * 获取推送id
     * @return  pushId
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
                .putBoolean(KEY_IS_BIND,isBind)
                .apply();
    }

    /**
     * 进行数据加载
     * @param context  上下文
     */
    public static void load(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID,"");
        isBind = sp.getBoolean(KEY_IS_BIND,false);
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

        return true;
    }


    /**
     * 是否已经绑定到了服务器
     * @return
     */
    public static boolean isBind(){
        return isBind;
    }

    /**
     * 设置绑定状态
     * @param isBind
     */
    public static void setBind(boolean isBind){
        Account.isBind = isBind;
        save(Factory.app());
    }
}
