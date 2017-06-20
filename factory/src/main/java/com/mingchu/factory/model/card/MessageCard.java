package com.mingchu.factory.model.card;

import com.google.gson.annotations.Expose;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.User;


import java.util.Date;


/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MessageCard {
    @Expose
    private String id;
    @Expose
    private String content;
    @Expose
    private String attach;
    @Expose
    private int type;
    @Expose
    private Date createAt;
    @Expose
    private String groupId;
    @Expose
    private String senderId;
    @Expose
    private String receiverId;

    private transient int status = Message.STATUS_DONE;
    private transient boolean uploaded = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Message build(User sender, User receiver, Group group) {
        Message message = new Message();
        message.setId(id);
        message.setContent(content);
        message.setAttach(attach);
        message.setType(type);
        message.setCreateAt(createAt);
        message.setGroup(group);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setStatus(status);
        return message;
    }
}
