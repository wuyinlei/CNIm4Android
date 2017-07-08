package com.mingchu.factory.presenter.group;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.view.MemberUserModel;

/**
 * Created by wuyinlei on 2017/7/8.
 *
 * @function 群成员的契约
 */

public interface GroupMembersContract {


    interface Presenter extends BaseContract.Presenter{

        //刷新的方法
        void refresh();

    }

    //界面
    interface View extends BaseContract.RecyclerView<Presenter,MemberUserModel>{

        String getGroupId();

    }
}
