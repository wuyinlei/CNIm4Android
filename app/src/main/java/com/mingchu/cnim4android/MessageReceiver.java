package com.mingchu.cnim4android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.mingchu.factory.Factory;
import com.mingchu.factory.data.helper.AccountHelper;
import com.mingchu.factory.persistence.Account;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * @function 个推广播接收器  接受推送的消息
 */

public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;

        Bundle bundle = intent.getExtras();

        //判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {

            case PushConsts.GET_CLIENTID: {
                Log.i(TAG, "GET_CLIENTID" + bundle.toString());
                //当id初始化的时候  获取设备id
                onClientInit(bundle.getString("clientid"));

                break;
            }
            case PushConsts.GET_MSG_DATA:

                //常规的消息
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String msg = new String(payload);
                    Log.e(TAG, "GET_MSG_DATA" + msg);
                    onMessageArrived(msg);
                }
                break;
            default:
                Log.i(TAG, "OTHER_MESSAGE" + bundle.toString());
                break;
        }
    }

    /**
     * 当clientId初始化的时候
     *
     * @param clientId 设备id
     */
    private void onClientInit(String clientId) {
        //设置设备id
        Account.setPushId(clientId);
        if (Account.isLogin()){
            //登录状态   进行一个PustId的绑定  没有登录的情况下 不能绑定PushID
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时候的处理
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        //交给Factory处理消息
        Factory.dispathPushMessage(message);
    }
}
