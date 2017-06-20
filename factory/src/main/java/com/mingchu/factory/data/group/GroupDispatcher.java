package com.mingchu.factory.data.group;

import com.mingchu.factory.data.helper.DbHelper;
import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.card.GroupMemberCard;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.GroupMember;
import com.mingchu.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wuyinlei on 2017/6/20.
 *
 * @function
 */

public class GroupDispatcher implements GroupCenter {
    private static GroupCenter instance;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static GroupCenter instance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null)
                    instance = new GroupDispatcher();
            }
        }
        return instance;
    }


    @Override
    public void dispatcher(GroupCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatcher(GroupMemberCard... models) {
        if (models == null || models.length == 0)
            return;
        executor.execute(new GroupMemberRspHandler(models));
    }

    private class GroupMemberRspHandler implements Runnable {
        private final GroupMemberCard[] models;

        GroupMemberRspHandler(GroupMemberCard[] models) {
            this.models = models;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard model : models) {
                User user = UserHelper.search(model.getUserId());
                Group group = GroupHelper.find(model.getGroupId());
                if (user != null && group != null) {
                    GroupMember member = model.build(group, user);
                    members.add(member);
                }
            }
            if (members.size() > 0)
                DbHelper.save(GroupMember.class, members.toArray(new GroupMember[0]));
        }
    }

    private class GroupHandler implements Runnable {
        private final GroupCard[] models;

        GroupHandler(GroupCard[] models) {
            this.models = models;
        }

        @Override
        public void run() {
            List<Group> groups = new ArrayList<>();
            for (GroupCard model : models) {
                User owner = UserHelper.search(model.getOwnerId());
                if (owner != null) {
                    Group group = model.build(owner);
                    groups.add(group);
                }
            }
            if (groups.size() > 0)
                DbHelper.save(Group.class, groups.toArray(new Group[0]));
        }
    }
}
