package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.fragment.message.ChatGroupFragment;
import com.mingchu.cnim4android.fragment.message.ChatUserFragment;
import com.mingchu.common.app.BaseSwipeBackActivity;
import com.mingchu.common.factory.model.Author;
import com.mingchu.factory.Factory;
import com.mingchu.factory.model.db.Group;

public class MessageActivity extends BaseSwipeBackActivity {

    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";

    //是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup = false;

    private Fragment mFragment;

    /**
     * 跳转到聊天消息界面
     *
     * @param context 上下文
     * @param author  联系人
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || author.getId() == null)
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 跳转到聊天消息界面
     *
     * @param context 上下文
     * @param group   群聊天
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || group.getId() == null)
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("");
        if (mIsGroup) {
            mFragment = new ChatGroupFragment();
        } else {
            mFragment = new ChatUserFragment();
        }

        //从activity传递参数到fragment里面
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID,mReceiverId);
        mFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,mFragment)
                .commit();


    }
}
