package com.mingchu.factory.presenter.account;

import com.mingchu.common.factory.presenter.BaseContract;

/**
 * Created by wuyinlei on 2017/6/29.
 *
 * @function 更新用户信息的契约
 */

public interface UpdateInfoContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 更新所有的
         *
         * @param name      用户名
         * @param photoPath 头像地址
         * @param desc      用户描述
         * @param isMan     是否是男生或者女生
         */
        void update(String name, String photoPath, String desc, boolean isMan);


        /**
         * 更新用户头像地址
         *
         * @param photoPath 头像地址
         */
        void updatePhoto(String photoPath);


        /**
         * 更新用户名和描述
         *
         * @param name 用户名
         * @param desc 用户描述
         */
        void updateBase(String name, String desc);

    }

    interface View extends BaseContract.View<Presenter> {

        void updateSuccess();
    }
}
