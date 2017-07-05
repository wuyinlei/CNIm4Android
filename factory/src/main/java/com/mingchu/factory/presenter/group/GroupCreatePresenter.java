package com.mingchu.factory.presenter.group;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.mingchu.common.factory.data.DataSource;
import com.mingchu.factory.presenter.BaseRecyclerPresenter;
import com.mingchu.factory.Factory;
import com.mingchu.factory.R;
import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.api.group.GroupCreateModel;
import com.mingchu.factory.model.card.GroupCard;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.model.db.view.UserSampleMode;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wuyinlei on 2017/7/2.
 *
 * @function 群创建的界面Presenter
 */

public class GroupCreatePresenter extends
        BaseRecyclerPresenter<User, GroupCreateContract.View>
        implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {

    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        //加载数据
        Factory.runOnAsync(loader);

    }

    private String uploadPicture(String path){
//        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(path)){
            //切换到主线程
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null)
                        view.showError(R.string.data_upload_error);
                }
            });
        }
        return path;
    }

    @Override
    public void create(final String name, final String desc, final String picture, final List<String> members) {
        GroupCreateContract.View view = getView();
        if (view == null)
            return;

        view.showLoading();

        //判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(picture)){
            view.showError(R.string.label_group_create_invalid);
        }
        //上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
               String url =  uploadPicture(picture);
                if (TextUtils.isEmpty(url))
                    return;

                GroupCreateModel model = new GroupCreateModel(name,desc,url,members);

                //请求接口
                GroupHelper.create(model,GroupCreatePresenter.this);

            }
        });



    }




    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleMode> userSampleModes = UserHelper.getSampleContact();

          List<User> models = new ArrayList<>();

            for (UserSampleMode sampleMode : userSampleModes) {
                User user = new User();
                user.setId(sampleMode.getId());
                user.setName(sampleMode.getName());
                user.setPortrait(sampleMode.getPortrait());
                models.add(user);
            }

            refreshData(models);

        }
    };


    //处理回调
    @Override
    public void onDataLoaded(GroupCard data) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null)
                    view.onCreateSuccessed();
            }
        });
    }

    @Override
    public void onDataNotAvaiable(@StringRes final int res) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null)
                    view.showError(res);
            }
        });
    }
}
