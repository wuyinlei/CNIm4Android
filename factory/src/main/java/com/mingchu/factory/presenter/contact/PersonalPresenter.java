package com.mingchu.factory.presenter.contact;

import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.factory.presenter.BasePresenter;
import com.mingchu.factory.Factory;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.net.UploadHelper;
import com.mingchu.factory.persistence.Account;
import com.raizlabs.android.dbflow.annotation.ModelView;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by wuyinlei on 2017/6/14.
 *
 * @function 用户界面的Presenter
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {

    private String userId;
    private User mUser;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        //个人界面数据请求

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    userId = getView().getUserId();
                    User user = UserHelper.searchFirstOfNet(userId);
                    onLoaded(getView(),user);
                }
            }
        });
    }

    private void onLoaded(final PersonalContract.View view, final User user) {
        this.mUser = user;
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        final boolean isFollow = isSelf || user.isFollow();
        final boolean allowSayHello = isFollow && !isSelf;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.allowSayHello(allowSayHello);
                view.setFollowStatus(isFollow);
                view.onloadDone(user);
            }
        });

    }

    @Override
    public User getUserPersonal() {
        return mUser;
    }
}
