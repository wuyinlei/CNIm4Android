package com.mingchu.factory.data.user;

import android.graphics.YuvImage;
import android.support.annotation.NonNull;

import com.mingchu.common.factory.data.DataSource;
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

public class ContactRespository implements ContactDataSource
        , QueryTransaction.QueryResultListCallback<User>, DbHelper.ChangedListener<User> {

    private DataSource.SuccessCallback<List<User>> callback;

//    private Set<User> users = new HashSet<>();

    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        this.callback = callback;

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

    @Override
    public void dispose() {
        this.callback = null;

        //取消对数据集合的监听
        DbHelper.removeChangedListener(User.class, this);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
//        users.addAll(tResult);  //缓存数据
        //数据库加载数据成功
//        if (callback != null) {
//            callback.onDataLoaded(tResult);
//        }

        if (tResult == null || tResult.size() == 0){
            users.clear();
            notifyDataChange();
            return;
        }



        //转变为数组
        User[] users = tResult.toArray(new User[0]);

        //回到数据集更新的操作中
        onDataSave(users);


    }






    @Override
    public void onDataDelete(User... list) {
        //当数据删除
        boolean isChanged = false;

        for (User user : list) {
            boolean remove = users.remove(user);
            if (remove)
                isChanged = true;
        }

        if (isChanged)
            notifyDataChange();

    }

    @Override
    public void onDataSave(User... list) {
        boolean isChanged = false;
        //当数据库变更的操作
        for (User user : list) {
            //是我关注的人 同时不是我自己
            if (isRequired(user)) {
                insertOrUpdate(user);
                isChanged = true;
            }
        }

        if (isChanged) {
            notifyDataChange();
        }
    }

    List<User> users = new LinkedList<>();

    private void insertOrUpdate(User user) {
        int index = indexOf(user);
        if (index >= 0) {
            replace(index, user);
        } else {
            insert(user);
        }
    }

    private void replace(int index, User user) {
        users.remove(index);
        users.add(index, user);
    }

    private void insert(User user) {
        users.add(user);
    }

    private int indexOf(User user) {
        int index = -1;
//        boolean index = users.contains(user);
        for (User user1 : users) {
            index++;
            if (user1.isSame(user)) {
                return index;
            }
        }
        return -1;
    }

    private void notifyDataChange() {
        if (callback != null) {
            callback.onDataLoaded(users);
        }
    }

    /**
     * 检查一个User是否是我关注的数据
     *
     * @param user User
     * @return true  是我关注的  false  不失
     */
    private boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
