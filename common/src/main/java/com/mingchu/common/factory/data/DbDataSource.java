package com.mingchu.common.factory.data;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/22.
 * <p>
 * 基础的数据库数据源接口定义
 */

public interface DbDataSource<Data> extends DataSource {

    /**
     * 一个基本的数据源加载方法
     *
     * @param callback callback 回调 一般回调到presenter
     */
    void load(SuccessCallback<List<Data>> callback);
}
