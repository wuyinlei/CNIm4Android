package com.mingchu.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wuyinlei on 2017/7/2.
 *
 * @function 添加群成员的model
 */

public class GroupMemberAddModel {


    private Set<String> users = new HashSet<>();

    public static boolean check(GroupMemberAddModel model){
        return !(model.getUsers() == null && model.getUsers().size() == 0);
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
