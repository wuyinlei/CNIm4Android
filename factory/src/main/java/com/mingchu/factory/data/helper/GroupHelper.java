package com.mingchu.factory.data.helper;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.Factory;
import com.mingchu.factory.R;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.group.GroupCreateModel;
import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.GroupMember;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.mingchu.factory.net.NetWork;
import com.mingchu.factory.net.RemoteService;
import com.mingchu.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.mingchu.factory.model.db.Group_Table;


import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wuyinlei on 2017/6/15.
 *
 * @function GroupHelper
 */

public class GroupHelper {


    /**
     * 先从本地查找群
     *
     * @param groupId 群id
     * @return Group
     */
    public static Group findFromLocal(String groupId) {

        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    public static List<GroupMember> getMembers(String id, int i) {
        return null;
    }

    public static List<MemberUserModel> getMemberUsers(String id, int i) {
        return null;
    }

    public static long getMemberCount(String id) {
        return 0;
    }


    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null)
            group = findFromNet(groupId);
        return group;
    }

    /**
     * 从网络中查找群
     *
     * @param groupId 群id
     * @return Group
     */
    public static Group findFromNet(String groupId) {

        RemoteService service = NetWork.remote();
        try {
            Response<RspModel<GroupCard>> response = service.groupInfo(groupId).execute();
            if (response.isSuccessful()) {
                GroupCard groupCard = response.body().getResult();
                if (groupCard != null) {

                    //数据库存储并且通知
                    Factory.getGroupCenter().dispatcher(groupCard);

                    User user = UserHelper.search(groupCard.getOwnerId());
                    if (user != null) {
                       return groupCard.build(user);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 群创建
     *
     * @param model    群创建的model
     * @param callback 回调
     */
    public static void create(final GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = NetWork.remote();
        service.createGroup(model)
                .enqueue(new Callback<RspModel<GroupCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                        RspModel<GroupCard> body = response.body();
                        if (body.success()) {
                            GroupCard groupCard = body.getResult();

                            Factory.getGroupCenter().dispatcher(groupCard);

                            callback.onDataLoaded(groupCard);
                        } else {
                            Factory.decodeRspCode(body, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                        callback.onDataNotAvaiable(R.string.data_network_error);
                    }
                });

    }
}
