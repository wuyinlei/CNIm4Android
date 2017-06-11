package com.mingchu.factory.presenter.search;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.card.UserCard;

import java.util.List;

/**
 * Created by wuyinlei on 2017/6/11.
 *
 * @function 搜索的契约
 */

public interface SearchContract {

    interface Presenter extends BaseContract.Presenter{

        void search(String content);
    }

    //搜索人的界面
    interface UserView extends BaseContract.View<Presenter>{

        void onSearchDone(List<UserCard> userCards);

    }

    //搜索群的界面
    interface GroupView extends BaseContract.View<Presenter>{

        void onSearchDone(List<GroupCard> groupCards);

    }

}
