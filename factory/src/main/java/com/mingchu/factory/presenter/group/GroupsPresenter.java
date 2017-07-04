package com.mingchu.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.factory.data.group.GroupsDataSource;
import com.mingchu.factory.data.group.GroupsRepository;
import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.presenter.BaseSourcePresenter;
import com.mingchu.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by wuyinlei on 2017/7/4.
 *
 * @function 我的群组的presenter
 */

public class GroupsPresenter extends BaseSourcePresenter<Group,Group,
        GroupsDataSource,GroupsContract.View> implements GroupsContract.Presenter{

    public GroupsPresenter(GroupsContract.View view) {
        super(view, new GroupsRepository());
    }

    @Override
    public void start() {
        super.start();

        GroupHelper.refreshGroups();

    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        GroupsContract.View view = getView();
        if (view == null)
            return;

        List<Group> oldItems = view.getRecyclerViewAadpter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(oldItems,groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //界面刷新
        refreshData(result,groups);

    }
}
