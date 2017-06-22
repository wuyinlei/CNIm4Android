package com.mingchu.factory.model.db;

import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Table(database = AppDatabase.class)
public class Message extends BaseDbModel<Message> implements Serializable {
    public static final int RECEIVER_TYPE_NONE = 1;
    public static final int RECEIVER_TYPE_GROUP = 2;

    public static final int TYPE_STR = 1;
    public static final int TYPE_PIC = 2;
    public static final int TYPE_FILE = 3;
    public static final int TYPE_AUDIO = 4;

    public static final int STATUS_DONE = 0;
    public static final int STATUS_CREATED = 1;
    public static final int STATUS_FAILED = 2;

    @PrimaryKey
    private String id;
    @Column
    private String content;
    @Column
    private String attach;
    @Column
    private int type;
    @Column
    private Date createAt;
    @Column
    private int status;

    @ForeignKey(tableClass = Group.class, stubbedRelationship = true)
    private Group group;

    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User sender;

    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User receiver;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * 当消息类型为普通消息(发送给人的消息)
     * 该方法用于返回 和我聊天的人是谁
     *
     * <p>
     *     和我聊天  要么对方是发送者 要么对方是接收者
     * </p>
     * @return  和我聊天的人
     */
    User getOther() {
        if (Account.getUserId().equals(sender.getId())) {
            return receiver;
        } else {
            return sender;
        }
    }

    /**
     * 构建一个简单的消息描述  用于简化消息显示
     * @return 一个消息描述
     */
    String getSampleContent() {
        if (type == TYPE_PIC)
            return "[图片]";
        else if (type == TYPE_AUDIO)
            return "🎵";
        else if (type == TYPE_FILE)
            return "📃";
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id != null ? id.equals(message.id) : message.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (attach != null ? attach.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isSame(Message oldT) {
        return Objects.equals(this, oldT);
    }

    @Override
    public boolean isUiContentSame(Message oldT) {
        boolean isSame;
        isSame = oldT == this || status == oldT.status;
        return isSame;
    }
}
