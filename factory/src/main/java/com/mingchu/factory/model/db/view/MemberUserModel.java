package com.mingchu.factory.model.db.view;

import com.mingchu.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * Created by wuyinlei on 2017/6/15.
 *
 * @function 群成员对应的用户简单信息表
 */

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {

    @Column
    public String userId;  //User  id   Member  userId
    @Column
    public String name;  //User
    @Column
    public String alias;  //Member
    @Column
    public String portrait;  //User

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
