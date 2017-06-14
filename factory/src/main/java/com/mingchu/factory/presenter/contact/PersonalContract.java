package com.mingchu.factory.presenter.contact;

import com.mingchu.common.factory.model.Author;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.User;

/**
 * Created by wuyinlei on 2017/6/14.
 *
 * @用户详情的契约
 */

public interface PersonalContract {

    interface Presenter extends BaseContract.Presenter{

        //获取用户信息
        User getUserPersonal();

    }

    interface View extends BaseContract.View<Presenter>{

        void onloadDone(User user);

        void allowSayHello(boolean isAllow);

        void setFollowStatus(boolean isFollow);

        String getUserId();

//        User getUser();

    }
}
