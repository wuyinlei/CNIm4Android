package com.mingchu.factory.data.user;

import com.mingchu.factory.model.card.UserCard;


/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */

public interface UserCenter {
    void dispatch(UserCard... models);
}
