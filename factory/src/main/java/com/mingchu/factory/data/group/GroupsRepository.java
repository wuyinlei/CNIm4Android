package com.mingchu.factory.data.group;

import android.text.TextUtils;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.data.BaseDbRespositroy;
import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Group_Table;


import java.util.List;

/**
 * Created by wuyinlei on 2017/7/4.
 *
 * @function 我的群组的数据仓库   是对GroupsDataSource的实现
 */

public class GroupsRepository extends BaseDbRespositroy<Group> implements GroupsDataSource {

    @Override
    public void load(SuccessCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();


    }

    @Override
    protected boolean isRequired(Group group) {

        //一个群的信息  只可能两种情况出现在数据库中
        //一个是你被别人加入群   第二个是你直接自己创建的群
        //无论什么情况 你拿到的都只是群的信息  没有成员的信息
        //你需要进行成员信息的初始化操作
        if (group.getGroupMemberCount() > 0) {
            //已经初始化了成员的信息
            group.holder = buildGroupHolder(group);
        } else {
            //待初始化的成员
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }

        return true;
    }

    /**
     * 初始化界面显示的成员信息
     *
     * @param group 群
     * @return 群成员列表
     */
    private String buildGroupHolder(Group group) {

        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0)
            return null;

        StringBuilder builder = new StringBuilder();
        for (MemberUserModel model : userModels) {
            builder.append(TextUtils.isEmpty(model.alias) ? model.name : model.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "), builder.length());

        return builder.toString();
    }
}
