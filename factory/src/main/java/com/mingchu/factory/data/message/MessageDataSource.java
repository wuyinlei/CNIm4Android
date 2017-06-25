package com.mingchu.factory.data.message;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.common.factory.data.DbDataSource;
import com.mingchu.factory.model.db.Message;

/**
 * Created by wuyinlei on 2017/6/24.
 *
 * @function 消息的数据源定义  他的实现是MessageRespository
 *
 * 关注的是Message表
 */

public interface MessageDataSource extends DbDataSource<Message> {
}
