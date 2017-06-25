package com.mingchu.factory.model.api.message;

import com.mingchu.factory.model.card.MessageCard;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.persistence.Account;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wuyinlei on 2017/6/25.
 *
 */
@SuppressWarnings("WeakerAccess")
public class MsgCreateModel {

    private String id;
    private String receiverId;
    private int receiverType;
    private String content;
    private String attach;
    private int type = Message.TYPE_STR;

    private MsgCreateModel() {
        id = UUID.randomUUID().toString();
    }

    public static MsgCreateModel buildWithMessage(Message message) {
        MsgCreateModel model = new MsgCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();
        if (message.getReceiver() != null) {
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }
        return model;
    }

    public static class Builder {
        private MsgCreateModel model;

        public Builder() {
            model = new MsgCreateModel();
        }

        public Builder receiver(String receiverId, int receiverType) {
            this.model.receiverId = receiverId;
            this.model.receiverType = receiverType;
            return this;
        }

        public Builder content(String content, int type) {
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        public Builder attach(String attach) {
            this.model.attach = attach;
            return this;
        }

        public MsgCreateModel build() {
            return this.model;
        }
    }

    public String getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public String getContent() {
        return content;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }


    public void refreshByCard() {
        if (this.card == null) {
            return;
        }
        this.content = card.getContent();
        this.attach = card.getAttach();
    }

    private MessageCard card;


    //构建一个Card
    public MessageCard buildCard() {
        if (this.card == null) {
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());
            // 如果是群消息
            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return this.card;
    }

}
