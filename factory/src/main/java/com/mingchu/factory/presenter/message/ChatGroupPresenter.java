package com.mingchu.factory.presenter.message;

import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.data.message.MessageGroupRespository;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.mingchu.factory.persistence.Account;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function 群聊天的逻辑
 */

public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
        implements ChatContract.Presenter {


    public ChatGroupPresenter(ChatContract.GroupView view,
                              String receiverId) {
        // view  数据源  接收者  接收者的类型
        super(view, new MessageGroupRespository(receiverId), receiverId, Message.RECEIVER_TYPE_GROUP);


    }

    @Override
    public void start() {
        super.start();
        //拿群的信息

        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            //  初始化操作
            ChatContract.GroupView view = getView();
            if (view == null)
                return;

            if (Account.getUserId().equalsIgnoreCase(group.getOwner().getId())) {
                //具有管理员权限
                view.showAdminOptions(true);

            } else {

            }

            view.onInit(group);

            List<MemberUserModel> models = group.getLatelyGroupMembers();

            final long memberCount = group.getGroupMemberCount();
            if (memberCount >= models.size()) {
                long moreCount = memberCount - models.size();
                view.onInitGroupMembers(models, moreCount);
            }


        }

    }
}
