package com.mingchu.factory.model.card;

import com.google.gson.annotations.Expose;
import com.mingchu.common.factory.model.Author;
import com.mingchu.factory.model.db.User;

import java.util.Date;

/**
 * Created by wuyinlei on 2017/6/11.
 */

public class UserCard implements Author{

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String portrait;
    @Expose
    private String desc;
    @Expose
    private int sex = 0;
    @Expose
    private boolean isFollow;
    @Expose
    private int follows;
    @Expose
    private int following;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }


    private User user;

    public User build() {
        if (this.user == null) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPhone(phone);
            user.setPortrait(portrait);
            user.setDesc(desc);
            user.setSex(sex);
            user.setFollow(isFollow);
            user.setFollows(follows);
            user.setFollowing(following);
            user.setModifyAt(modifyAt);
            this.user = user;
        }
        return this.user;
    }
}
