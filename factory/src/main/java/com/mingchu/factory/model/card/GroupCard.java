package com.mingchu.factory.model.card;

import com.google.gson.annotations.Expose;

import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.model.db.User;

import java.util.Date;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public class GroupCard {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String desc;
    @Expose
    private String picture;
    @Expose
    private String ownerId;
    @Expose
    private int notifyLevel;
    @Expose
    private Date joinAt;
    @Expose
    private Date modifyAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public Date getJoinAt() {
        return joinAt;
    }

    public void setJoinAt(Date joinAt) {
        this.joinAt = joinAt;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Group build(User owner) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setDesc(desc);
        group.setPicture(picture);
        group.setNotifyLevel(notifyLevel);
        group.setJoinAt(joinAt);
        group.setModifyAt(modifyAt);
        group.setOwner(owner);
        return group;
    }
}
