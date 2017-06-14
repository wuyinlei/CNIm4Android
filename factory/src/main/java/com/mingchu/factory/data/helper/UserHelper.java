package com.mingchu.factory.data.helper;

import android.net.Network;
import android.text.TextUtils;
import android.util.Log;
import com.mingchu.factory.model.db.User_Table;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.Factory;
import com.mingchu.factory.R;
import com.mingchu.factory.model.api.RspModel;
import com.mingchu.factory.model.api.user.UserUpdateModel;
import com.mingchu.factory.model.card.UserCard;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.net.NetWork;
import com.mingchu.factory.net.RemoteService;
import com.mingchu.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wuyinlei on 2017/6/12.
 * <p>
 * 用户辅助类
 */

public class UserHelper {

    /**
     * 用户信息更新方法
     *
     * @param name     名字
     * @param portrait 头像
     * @param desc     描述
     * @param sex      性别
     * @param callback CallBack
     */
    public static void update(final String name, final String portrait, final String desc, final int sex, final DataSource.Callback<User> callback) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String urlPortrait = UploadHelper.uploadPortrait(portrait);
                if (TextUtils.isEmpty(urlPortrait)) {
                    callback.onDataNotAvaiable(R.string.data_upload_error);
                    return;
                }

                UserUpdateModel infoModel = new UserUpdateModel(name, urlPortrait, desc, sex);
                RemoteService remoteService = NetWork.remote();
                Call<RspModel<UserCard>> rspModelCall = remoteService.userUpdate(infoModel);
                try {
                    Response<RspModel<UserCard>> response = rspModelCall.execute();
                    RspModel<UserCard> rspModel = response.body();
                    if (rspModel.success()) {
                        final UserCard model = rspModel.getResult();
                        Factory.getUserCenter().dispatch(model);
                        callback.onDataLoaded(model.build());
                    } else {
                        Factory.decodeRspCode(rspModel, callback);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onDataNotAvaiable(R.string.data_network_error);
                }
            }
        });
    }


    /**
     * 搜搜方法
     *
     * @param content  搜索关键字
     * @param callback 回调接口
     */
    public static Call search(String content, final DataSource.Callback<List<UserCard>> callback) {

        Call<RspModel<List<UserCard>>> call = NetWork.remote().userSearch(content);

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call,
                                   Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> body = response.body();
                if (body.success()) {
                    List<UserCard> result = body.getResult();
                    callback.onDataLoaded(result);
                } else {
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvaiable(R.string.data_network_error);
            }
        });

        return call;
    }


    /**
     * 关注人的方法
     *
     * @param userId   关注的人的id
     * @param callback 回调接口
     */
    public static void follow(String userId, final DataSource.Callback<UserCard> callback) {
        NetWork.remote().userFollow(userId)
                .enqueue(new Callback<RspModel<UserCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                        RspModel<UserCard> body = response.body();
                        if (body.success()) {

                            User user = body.getResult().build();
                            user.save();  //保存到本地数据库
                            //// TODO: 2017/6/12 通知联系人列表刷新

                            callback.onDataLoaded(body.getResult());
                        } else {
                            Factory.decodeRspCode(body, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                        callback.onDataNotAvaiable(R.string.data_network_error);
                    }
                });

    }


    /**
     * 联系人列表  刷新联系人
     *
     * @param callback 回调接口
     */
    public static void refreshContact(final DataSource.Callback<List<UserCard>> callback) {

        Call<RspModel<List<UserCard>>> call = NetWork.remote().contactUsers();

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call,
                                   Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> body = response.body();
                if (body.success()) {
                    List<UserCard> result = body.getResult();
                    callback.onDataLoaded(result);
                } else {
                    Factory.decodeRspCode(body, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvaiable(R.string.data_network_error);
            }
        });

    }

    /**
     * 搜索一个用户 有限本地缓存  如果没有就走网络
     *
     * @param userId 用户id
     * @return User
     */
    public static User searchUser(String userId) {
        User user = findFormLocal(userId);
        if (user == null)
            return findFormNet(userId);
        return user;
    }


    /**
     * 搜索一个用户  优先网络  没有就从本地缓存
     *
     * @param userId 用户id
     * @return User
     */
    public static User searchFirstOfNet(String userId) {

        return findFormNet(userId);
    }

    public static User findFormNet(String userId){
        RemoteService service = NetWork.remote();

        try {
            Response<RspModel<UserCard>> response = service.userInfo(userId).execute();
            UserCard card = response.body().getResult();
            if (card != null){
                //数据库的刷新  // TODO: 2017/6/15   数据库通知 但是没有刷新
                User user = card.build();
                user.save();

                return user;
            }

        }catch (Exception e){
            Log.d("UserHelper", e.getMessage());
        }
        return null;
    }

    /**
     * 从本地查询
     *
     * @param userId 用户id
     * @return User
     */
    public static User findFormLocal(String userId) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }


}
