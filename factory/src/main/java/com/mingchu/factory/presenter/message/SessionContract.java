package com.mingchu.factory.presenter.message;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.db.Session;

/**
 * Created by wuyinlei on 2017/6/25.
 *
 * @function 最近会话列表的契约
 */

public interface SessionContract {

    interface Presenter extends BaseContract.Presenter{

    }


    interface View extends BaseContract.RecyclerView<Presenter,Session>{

    }
}
