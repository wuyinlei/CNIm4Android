package com.mingchu.factory.data.helper;

import com.mingchu.factory.model.db.Session;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Session_Table;


/**
 * Created by wuyinlei on 2017/6/20.
 *
 * @function SessionHelper 会话辅助工具类
 */

public class SessionHelper {

    /**
     * 从本地查询session
     *
     * @param id id
     * @return Session
     */
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
