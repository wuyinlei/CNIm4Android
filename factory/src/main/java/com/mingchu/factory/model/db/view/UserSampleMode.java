package com.mingchu.factory.model.db.view;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.model.Author;
import com.mingchu.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * Created by wuyinlei on 2017/7/2.
 *
 * @function 用户基础信息的Model  可以和数据库进行查询
 */
@QueryModel(database = AppDatabase.class)
public class UserSampleMode implements Author {

    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String portrait;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPortrait() {
        return portrait;
    }

    @Override
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

}
