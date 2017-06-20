package com.mingchu.factory.model.db;

import android.text.TextUtils;

import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.data.helper.MessageHelper;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


import java.util.Date;
import java.util.Objects;


@Table(database = AppDatabase.class)
public class Session extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Session> {
    @PrimaryKey
    private String id;  //Id   Message中的接收者User的id或者群id
    @Column
    private String picture;  //图片  接收者用户的头像或者群的图片
    @Column
    private String title; //标题  用户的名称  或者群的名称
    @Column
    private String content;  //显示在界面上的简单内容  是Message的一个描述
    @Column
    private int receiverType = Message.RECEIVER_TYPE_NONE;  //类型  对应人  或者群
    @Column
    private int unReadCount;  //未读消息数量  当灭有在当前界面的时候 应当增加未读数量
    @Column
    private Date modifyAt;  //最后更改时间
    @ForeignKey(tableClass = Message.class)
    private Message message;   //对应的消息 外键为Message的Id

    public Session() {

    }

    public Session(Identify identify) {
        this.id = identify.id;
        this.receiverType = identify.type;
    }

    public Session(Message message) {
        if (message.getGroup() == null) {
            receiverType = Message.RECEIVER_TYPE_NONE;
            User other = message.getOther();
            id = other.getId();
            picture = other.getPortrait();
            title = other.getName();
        } else {
            receiverType = Message.RECEIVER_TYPE_GROUP;
            id = message.getGroup().getId();
            picture = message.getGroup().getPicture();
            title = message.getGroup().getName();
        }
        this.message = message;
        this.content = message.getSampleContent();
        this.modifyAt = message.getCreateAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    /**
     * 刷新到最新状态
     */
    public void refreshToNow() {
        Message message;
        if (receiverType == Message.RECEIVER_TYPE_GROUP) {
            message = MessageHelper.findLastWithGroup(id);

            if (message == null) {
                if (TextUtils.isEmpty(this.picture)
                        || TextUtils.isEmpty(this.title)) {
                    Group group = GroupHelper.findFromLocal(id);
                    if (group != null) {
                        this.picture = group.getPicture();
                        this.title = group.getName();
                    }
                }
                this.message = null;
                this.content = "";
                this.modifyAt = new Date(System.currentTimeMillis());
            } else {
                if (TextUtils.isEmpty(this.picture)
                        || TextUtils.isEmpty(this.title)) {
                    Group group = message.getGroup();
                    group.load();
                    this.picture = group.getPicture();
                    this.title = group.getName();
                }
                this.message = message;
                this.content = message.getSampleContent();
                this.modifyAt = message.getCreateAt();
            }
        } else {
            message = MessageHelper.findLastWithUser(id);

            if (message == null) {
                if (TextUtils.isEmpty(this.picture)
                        || TextUtils.isEmpty(this.title)) {
                    User user = UserHelper.findFromLocal(id);
                    if (user != null) {
                        this.picture = user.getPortrait();
                        this.title = user.getName();
                    }
                }
                this.content = "";
                this.message = null;
                this.modifyAt = new Date(System.currentTimeMillis());
            } else {
                if (TextUtils.isEmpty(this.picture)
                        || TextUtils.isEmpty(this.title)) {
                    User other = message.getOther();
                    other.load();
                    this.picture = other.getPortrait();
                    this.title = other.getName();
                }
                this.message = message;
                this.content = message.getSampleContent();
                this.modifyAt = message.getCreateAt();
            }
        }
    }

    /**
     * 对于一个消息  我们提取主要部分  用于和Session进行对应
     *
     * @param message 消息Model
     * @return 返回一个Session.Identify
     */
    public static Identify createSessionIdentify(Message message) {
        Identify identify = new Identify();
        if (message.getGroup() == null) {
            identify.type = Message.RECEIVER_TYPE_NONE;
            User other = message.getOther();
            identify.id = other.getId();
        } else {
            identify.type = Message.RECEIVER_TYPE_GROUP;
            identify.id = message.getGroup().getId();
        }
        return identify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return receiverType == session.receiverType
                && (id != null ? id.equals(session.id) : session.id == null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + receiverType;
        return result;
    }

    @Override
    public boolean isSame(Session oldT) {
        return Objects.equals(this, oldT);
    }

    @Override
    public boolean isUiContentSame(Session oldT) {
        return this.content.equals(oldT.content)
                && Objects.equals(this.modifyAt, oldT.modifyAt);
    }

    /**
     * 对于会话信息 最重要的部分进行提取
     * 其中我们主要关注两个点:
     * 一个会话最重要的是标示和人聊天还是在和群聊天
     * 所以对于这点  Id存储的是人或者群的id
     * 紧跟着Type  存储的是具体的类型(人、群)
     * equals  和  hashCode也是对这两个字段进行判断
     */
    public static class Identify {
        public String id;
        public int type;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Identify identify = (Identify) o;
            return type == identify.type && (id != null ? id.equals(identify.id) : identify.id == null);
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + type;
            return result;
        }
    }
}