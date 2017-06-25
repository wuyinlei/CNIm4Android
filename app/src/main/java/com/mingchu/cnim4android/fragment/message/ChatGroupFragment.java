package com.mingchu.cnim4android.fragment.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingchu.cnim4android.R;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.presenter.message.ChatContract;

/**
 * @author wuyinlei
 * @function 群聊天
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }


    @Override
    public void onInit(Group group) {

    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }
}
