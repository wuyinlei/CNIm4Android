package com.mingchu.factory.data.message;

import android.support.annotation.NonNull;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.data.BaseDbRespositroy;
import com.mingchu.factory.model.db.Message;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function MessageRespository
 * 跟某人聊天的时候的聊天列表
 * 关注的内容一定是我发给这个人的或者是他发送给我的
 */

public class MessageRespository extends BaseDbRespositroy<Message> implements MessageDataSource {


    //聊天的对象id
    private String receiverId;

    public MessageRespository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Message message) {

        //receiverId  如果是发送者  group是为空  一定是发送给我的消息

        //如果消息的接收者不为空  那么一定是发送给某个人的  这个人只能是我或者是某个人

        //如果这个人就是receiverId  那么就是我需要关注的消息
        return (receiverId.equalsIgnoreCase(message.getSender().getId())
                && message.getGroup() == null) ||
                (message.getReceiver() != null &&
                        receiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {

        Collections.reverse(tResult);  //反转返回的集合  然后在进行父类里面的数据调度

        super.onListQueryResult(transaction, tResult);

    }
}
