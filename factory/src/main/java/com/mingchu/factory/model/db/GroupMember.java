package com.mingchu.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;


@Table(database = AppDatabase.class)
public class GroupMember extends BaseModel {
    public static final int NOTIFY_LEVEL_INVALID = -1;
    public static final int NOTIFY_LEVEL_NONE = 0;

    @PrimaryKey
    private String id;
    @Column
    private String alias;
    @Column
    private boolean isAdmin;
    @Column
    private boolean isOwner;
    @Column
    private Date modifyAt;

    @ForeignKey(tableClass = Group.class, stubbedRelationship = true)
    private Group group;
    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
