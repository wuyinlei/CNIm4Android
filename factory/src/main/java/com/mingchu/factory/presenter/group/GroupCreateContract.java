package com.mingchu.factory.presenter.group;

import com.mingchu.common.factory.model.Author;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.User;

import java.util.List;
import java.util.Set;

/**
 * Created by wuyinlei on 2017/7/2.
 *
 * @function 群组创建契约
 */

public interface GroupCreateContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 创建
         *  @param name    群组名字
         * @param desc    群组描述
         * @param picture 群组头像
         * @param members
         */
        void create(String name, String desc, String picture, List<String> members);


    }

    interface View extends BaseContract.RecyclerView<Presenter, User> {


        //创建成功
        void onCreateSuccessed();


    }



}
