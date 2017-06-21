package com.mingchu.factory.data.user;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.model.db.User;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/21.
 *
 * @function 联系人数据源
 */

public interface ContactDataSource {

    //对数据加载的一个职责
    void load(DataSource.SuccessCallback<List<User>> callback);

    //取消监听
    void dispose();

}
