package com.mingchu.factory.presenter.message;

import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.data.message.MessageDataSource;
import com.mingchu.factory.data.message.MessageRespository;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.User;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function 聊天的presenter
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {


    public ChatUserPresenter(ChatContract.UserView view,
                             String receiverId) {
        // view  数据源  接收者  接收者的类型
        super(view, new MessageRespository(receiverId), receiverId, Message.RECEIVER_TYPE_NONE);


    }

    @Override
    public void start() {
        super.start();

        //从本地获取这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);  //初始化User

    }
}
