package com.mingchu.factory.data.message;

import android.text.TextUtils;

import com.mingchu.factory.data.helper.DbHelper;
import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.data.helper.MessageHelper;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.data.user.UserCenter;
import com.mingchu.factory.data.user.UserDispatcher;
import com.mingchu.factory.model.card.MessageCard;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wuyinlei on 2017/6/20.
 *
 * @function 实现类
 */

public class MessageDispatcher implements MessageCenter {

    private static MessageCenter instance;
    //线程池
    private Executor executor = Executors.newSingleThreadExecutor();

    //单利
    public static MessageCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null)
                    instance = new MessageDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        executor.execute(new MessageCardHandler(cards));
    }

    /**
     * 消息卡片的线程调度  处理会触发runnable方法
     */
    private class MessageCardHandler implements Runnable{

        private final MessageCard[] cards;

        MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            for (MessageCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId())))
                    continue;
                //消息卡片有可能是推送过来的  也有可能是自己造的
                //推送过来的代表服务器一定有  我们可以查询到   本地有可能有  可能没有
                //如果是自己造的   那么 先存储本地  后发送网络
                //发送消息流程:  写消息--->存储本地--->发送网络--->网络返回--->刷新本地状态
                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {
                    // 如果已经完成则不做处理
                    //消息字段从发送后就不变化了 如果收到了消息
                    //本地有  同事本地显示消息状态为完成状态 则不必处理
                    //因此此时回来的消息和本地一定一模一样
                    if (message.getStatus() == Message.STATUS_DONE)
                        continue;
                    // 新状态为完成才更新服务器时间，不然不做更新
                    if (card.getStatus() == Message.STATUS_DONE)
                        //
                        message.setCreateAt(card.getCreateAt());


                    // 更新一些会变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                } else {
                    //没找到本地消息  初次在数据库存储
                    //发送者
                    User sender = UserHelper.search(card.getSenderId());
                    //接收者
                    User receiver = null;
                    //群接收者
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }
                    if (receiver == null && group == null)
                        continue;

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);
            }
            if (messages.size() > 0)
                DbHelper.save(Message.class, messages.toArray(new Message[0]));
        }
    }
}
