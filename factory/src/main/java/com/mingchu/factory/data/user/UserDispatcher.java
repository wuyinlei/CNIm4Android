package com.mingchu.factory.data.user;

import android.text.TextUtils;


import com.mingchu.factory.data.helper.DbHelper;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */

public class UserDispatcher implements UserCenter {

    private static UserCenter instance;
    //线程池
    private Executor executor = Executors.newSingleThreadExecutor();

    //单利
    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null)
                    instance = new UserDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度的时候会触发run方法
     */
    private class UserCardHandler implements Runnable {

        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                users.add(card.build());
            }
            //进行数据库存储并分发通知  异步
            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
