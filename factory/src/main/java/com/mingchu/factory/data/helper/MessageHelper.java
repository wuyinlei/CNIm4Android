package com.mingchu.factory.data.helper;

import com.mingchu.factory.Factory;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.message.MsgCreateModel;
import com.mingchu.factory.model.card.MessageCard;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.net.NetWork;
import com.mingchu.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Message_Table;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by wuyinlei on 2017/6/15.
 *
 * @function 消息工具类
 */

public class MessageHelper {


    public static Message findLastWithGroup(String id) {
        return null;
    }

    public static Message findLastWithUser(String id) {
        return null;
    }

    /**
     * 从本地拿取数据
     *
     * @param id id
     * @return Message
     */
    public static Message findFromLocal(String id) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是异步进行的
    public static void push(final MsgCreateModel model) {

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {

                //成功状态：如果是一个已经发送过的消息  则不能重新发送
                //正在发送状态  如果是一个消息正在发送  则不能重新发送
                //如果是一个已经发送过的消息  则不能重新发送
                Message message = findFromLocal(model.getId());

                //已经发送过的
                if (message != null && message.getStatus() != Message.STATUS_FAILED){
                    return;
                }

                //如果是文件类型(语音 图片  文件)  需要先上传后才发送

                //我们发送的时候要对界面进行更新  通过Card

                //正常情况的消息(文字)  直接发送

                final MessageCard card = model.buildCard();

                Factory.getMessageCenter().dispatch(card);

                RemoteService service = NetWork.remote();
                service.msgPush(model)
                        .enqueue(new Callback<RspModel<MessageCard>>() {
                            @Override
                            public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                                RspModel<MessageCard> rspModel = response.body();
                                if (rspModel != null && rspModel.success()) {

                                    MessageCard newCard = rspModel.getResult();
                                    if (newCard != null) {
                                        //成功的时候进行一次调度
                                        Factory.getMessageCenter().dispatch(newCard);
                                    }

                                } else {

                                    //解析一下是否是账户异常
                                    Factory.decodeRspCode(rspModel, null);

                                    //走失败流程
                                    onFailure(call, null);
                                }
                            }

                            @Override
                            public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                                //设置MessageCard的发送状态为失败状态
                                card.setStatus(Message.STATUS_FAILED);
                                //然后重新发送
                                Factory.getMessageCenter().dispatch(card);
                            }
                        });

            }
        });


    }
}
