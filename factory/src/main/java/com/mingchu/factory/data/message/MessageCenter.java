package com.mingchu.factory.data.message;

import com.mingchu.factory.model.card.MessageCard;

/**
 * Created by wuyinlei on 2017/6/20.
 *
 * @function 进行消息卡片的消费
 */

public interface MessageCenter {

    void dispatch(MessageCard... cards);
}
