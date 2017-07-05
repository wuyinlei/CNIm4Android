package com.mingchu.factory.data.message;

import android.support.annotation.NonNull;

import com.mingchu.factory.data.BaseDbRespositroy;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function MessageRespository
 * 跟群聊天的时候的聊天列表
 *
 * 关注的内容一定是我发给这个群的或者是别人发送到群的
 */

public class MessageGroupRespository extends BaseDbRespositroy<Message> implements MessageDataSource {


    //聊天的群id
    private String receiverId;

    public MessageGroupRespository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);



    }

    @Override
    protected boolean isRequired(Message message) {

        //receiverId  如果是发送者  group是为空  一定是发送给我的消息

        //如果消息的接收者不为空  那么一定是发送给某个人的  这个人只能是我或者是某个人

        //如果这个人就是receiverId  那么就是我需要关注的消息
        return false;
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {

        Collections.reverse(tResult);  //反转返回的集合  然后在进行父类里面的数据调度

        super.onListQueryResult(transaction, tResult);

    }
}
