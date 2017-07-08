package com.mingchu.factory.presenter.group;

import com.mingchu.factory.Factory;
import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.mingchu.factory.model.db.view.UserSampleMode;
import com.mingchu.factory.presenter.BaseRecyclerPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyinlei on 2017/7/8.
 *
 * @function Presenter
 */

public class GroupMembersPresenter extends
        BaseRecyclerPresenter<MemberUserModel,GroupMembersContract.View>
implements GroupMembersContract.Presenter{


    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        start();

        Factory.runOnAsync(loader);
    }




    private Runnable loader = new Runnable() {
        @Override
        public void run() {

            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = getView().getGroupId();

            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId,-1);

            refreshData(models);

        }
    };
}
