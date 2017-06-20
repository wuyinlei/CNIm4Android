package com.mingchu.factory.data.user;

import com.mingchu.factory.model.card.UserCard;


/**
 * 用户中心的基本定义
 */

public interface UserCenter {
    //分发处理一堆卡片  并更新到数据库
    void dispatch(UserCard... models);
}
