package com.mingchu.factory.data.group;

import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.card.GroupMemberCard;

/**
 * Created by wuyinlei on 2017/6/20.
 *
 * @function 群中心的接口定义
 */

public interface GroupCenter {

    void dispatcher(GroupCard... cards);

    void dispatcher(GroupMemberCard... cards);
}
