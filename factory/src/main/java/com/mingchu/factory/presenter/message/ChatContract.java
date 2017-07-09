package com.mingchu.factory.presenter.message;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function 聊天契约
 */

public interface ChatContract {

    interface Presenter extends BaseContract.Presenter{

        //发送文字
        void pushText(String content);

        //发送语音
        void pushAudio(String path,long time);

        //发送图片
        void pushImages(String[] paths);

        //重新发送一个消息  是否调度成功
        boolean rePush(Message message);
    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter,Message>{

        //初始化的model
        void onInit(InitModel model);

    }

    //人聊天界面
    interface UserView extends View<User>{


    }

    //群聊天界面
    interface GroupView extends View<Group>{

        //显示是否是管理员
        void showAdminOptions(boolean isAdmin);

        //初始化成员
        void onInitGroupMembers(List<MemberUserModel> memberUserModels,long moreCount);

    }
}
