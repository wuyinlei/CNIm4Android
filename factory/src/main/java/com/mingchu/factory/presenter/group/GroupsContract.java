package com.mingchu.factory.presenter.group;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.Group;

/**
 * Created by wuyinlei on 2017/7/4.
 *
 * @function 群组契约
 */

public interface GroupsContract {

    interface Presenter extends BaseContract.Presenter{


    }

    interface View extends BaseContract.RecyclerView<Presenter,Group>{


    }
}
