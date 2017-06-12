package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.mingchu.cnim4android.R;
import com.mingchu.common.factory.model.Author;

public class MessageActivity extends Activity {


    /**
     * 跳转到聊天消息界面
     *
     * @param context 上下文
     * @param author  联系人
     */
    public static void show(Context context, Author author) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

}
