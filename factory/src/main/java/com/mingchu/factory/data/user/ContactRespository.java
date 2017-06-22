package com.mingchu.factory.data.user;

import android.graphics.YuvImage;
import android.support.annotation.NonNull;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.data.BaseDbRespositroy;
import com.mingchu.factory.data.helper.DbHelper;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.mingchu.factory.model.db.User_Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wuyinlei on 2017/6/21.
 *
 * @function 联系人仓库
 */

public class ContactRespository extends BaseDbRespositroy<User> implements ContactDataSource {


    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        super.load(callback);
        //对数据辅助工具类添加一个数据更新的监听
        DbHelper.addChangedListener(User.class, this);
        //加载本地数据库
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    /**
     * 检查一个User是否是我关注的数据
     *
     * @param user User
     * @return true  是我关注的  false  不失
     */
    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
