package com.mingchu.factory.model.api.group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wuyinlei on 2017/7/2.
 *
 * @function 群组创建的model
 */

public class GroupCreateModel {

    private String name;  //群组名
    private String desc;  //群描述
    private String picture;  //群头像
    private List<String> users = new ArrayList<>();

    public GroupCreateModel(String name, String desc, String picture, List<String> users) {
        this.name = name;
        this.desc = desc;
        this.picture = picture;
        this.users = users;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
