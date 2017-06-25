package com.mingchu.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.mingchu.factory.data.helper.MessageHelper;
import com.mingchu.factory.data.message.MessageDataSource;
import com.mingchu.factory.model.api.message.MsgCreateModel;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.presenter.BaseSourcePresenter;
import com.mingchu.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function 聊天presenter的基础类
 */

public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {

    protected String mReceiverId;
    protected int mReceiverType;

    public ChatPresenter(View view, MessageDataSource source, String receiverId, int receiverType) {
        super(view, source);

        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        View view = getView();
        if (view == null)
            return;

        //拿到老数据
        List<Message> oldItems = view.getRecyclerViewAadpter().getItems();
        //差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(oldItems, messages);
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, messages);  //进行界面刷新
    }

    @Override
    public void pushText(String content) {
        //构建model
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId,mReceiverType)
                .content(content,Message.TYPE_STR)
                .build();
        //发送消息
        MessageHelper.push(model);

    }

    @Override
    public void pushAudio(String path) {
        // TODO: 2017/6/25   发送语音
    }

    @Override
    public void pushImages(String[] paths) {
        // TODO: 2017/6/25 发送图片
    }

    @Override
    public boolean rePush(Message message) {
        return false;
    }
}
