package com.mingchu.factory.model.card;

import com.google.gson.annotations.Expose;

import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.model.db.GroupMember;
import net.qiujuer.italker.factory.model.db.User;

import java.util.Date;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public class GroupMemberCard {
    @Expose
    private String id;
    @Expose
    private String alias;
    @Expose
    private boolean isAdmin;
    @Expose
    private boolean isOwner;
    @Expose
    private String userId;
    @Expose
    private String groupId;
    @Expose
    private Date modifyAt;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public GroupMember build(Group group, User user) {
        GroupMember member = new GroupMember();
        member.setId(this.id);
        member.setAlias(this.alias);
        member.setAdmin(this.isAdmin);
        member.setOwner(this.isOwner);
        member.setModifyAt(this.modifyAt);
        member.setGroup(group);
        member.setUser(user);
        return member;
    }
}
