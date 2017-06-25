package com.mingchu.factory.data.message;

import android.support.annotation.NonNull;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.data.BaseDbRespositroy;
import com.mingchu.factory.model.db.Session;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;


import java.util.Collections;
import java.util.List;

/**
 * Created by wuyinlei on 2017/6/25.
 *
 * @function 对DataSource的实现
 */

public class SessionRespository extends BaseDbRespositroy<Session> implements SessionDataSource{


    @Override
    public void load(SuccessCallback<List<Session>> callback) {
        super.load(callback);

        //对数据库的一种查询
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt,true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Session session) {
        //所有的会话我都需要  不需要过滤
        return true;
    }


    @Override
    protected void insert(Session session) {
        super.insert(session);
        if (session == null)
            return;
        //复写方法  让新的数据加到头部
        mDatas.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        //复写数据库回来的方法
        Collections.reverse(tResult);

        super.onListQueryResult(transaction, tResult);

    }
}
