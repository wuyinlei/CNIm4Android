package com.mingchu.factory.presenter.contact;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.User;

import java.net.UnknownServiceException;
import java.util.List;

/**
 * Created by wuyinlei on 2017/6/14.
 *
 * @联系人契约
 */

public interface ConactContract {

    /**
     * 什么都不需要做
     */
    interface Presenter extends BaseContract.Presenter{

    }

    /**
     * 都在基类里面完成了
     */
    interface View extends BaseContract.RecyclerView<Presenter,User>{


    }
}
