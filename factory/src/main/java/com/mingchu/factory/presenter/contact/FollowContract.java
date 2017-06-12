package com.mingchu.factory.presenter.contact;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.model.card.UserCard;

/**
 * Created by wuyinlei on 2017/6/12.
 * <p>
 * 联系人契约
 */

public interface FollowContract {

    /**
     * 任务调度者
     */
    interface Presenter extends BaseContract.Presenter {
        //关注一个人
        void follow(String id);
    }


    interface View extends BaseContract.View<Presenter> {
        /**
         * 成功的情况下返回用户信息
         *
         * @param userCard UserCard
         */
        void onFollowSuccess(UserCard userCard);
    }

}
