package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.common.app.BaseSwipeBackActivity;
import com.mingchu.common.app.PresenterToolbarActivity;
import com.mingchu.common.app.ToolbarActivity;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.fglass.Fglass;
import com.mingchu.common.widget.swipeback.SwipeBackActivityBase;
import com.mingchu.factory.Factory;
import com.mingchu.factory.data.helper.UserHelper;
import com.mingchu.factory.model.db.User;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.presenter.contact.PersonalContract;
import com.mingchu.factory.presenter.contact.PersonalPresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {


    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    @BindView(R.id.im_header)
    ImageView mHeader;

    @BindView(R.id.tv_fglass)
    TextView mTvFglass;


    @BindView(R.id.iv_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_name)
    TextView mName;

    @BindView(R.id.txt_desc)
    TextView mDesc;

    @BindView(R.id.txt_follows)
    TextView mFollows;

    @BindView(R.id.txt_following)
    TextView mFollowing;

    @BindView(R.id.btn_say_hello)
    Button mSayHello;

    @BindView(R.id.btn_logout)
    Button mBtLogout;

    private boolean mIsFollow;
    private MenuItem mFollow;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollow = menu.findItem(R.id.action_follow);
        changeFollowStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            if (userId.equalsIgnoreCase(Account.getUserId())) {
                //如果是自己  则点击的应该是编辑按钮  调转到编辑用户信息界面
                UserActivity.show(this);

            } else {
                // 如果看的不是自己的信息  则点击 应该是关注和取消关注
                // TODO: 2017/6/29

            }
            //进行关注的操作
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.btn_say_hello)
    void onSayHelloClick() {
        //发起聊天  必须是自己以外的用户id
        MessageActivity.show(this, mPresenter.getUserPersonal());
    }


    @OnClick(R.id.btn_logout)
    void onLogout() {
        Factory.deleteUserInfo();
        AccountActivity.show(this);
    }


    //改变关注状态
    private void changeFollowStatus() {
        if (mFollow == null)
            //
            return;

        if (!Account.getUserId().equalsIgnoreCase(userId)) {

            mFollow.setEnabled(!mIsFollow);

            Drawable menuIcon = mIsFollow ? getResources().getDrawable(R.drawable.ic_favorite) :
                    getResources().getDrawable(R.drawable.ic_favorite_border);
            menuIcon = DrawableCompat.wrap(menuIcon).mutate();
            DrawableCompat.setTint(menuIcon, UiCompat.getColor(getResources(), R.color.white));
            mFollow.setIcon(menuIcon);
        } else {
            mFollow.setIcon(getResources().getDrawable(R.drawable.ic_edit));

        }


    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("");

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                final String portrait = UserHelper.search(userId).getPortrait();

                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {

                        if (!TextUtils.isEmpty(portrait)){
                            Glide.with(PersonalActivity.this)
                                    .load(portrait)
                                    .asBitmap()
                                    .centerCrop()
                                    .error(R.drawable.default_user_img)
                                    .placeholder(R.drawable.default_user_img)
                                    .into(mHeader);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                Fglass.blur(mHeader,mTvFglass,2,8);
                            }
                        },100);
                    }
                });
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        if (Account.isLogin()) {  //登录了才能去请求网络
            mPresenter.start();
        } else {
            AccountActivity.show(this);
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    public void onloadDone(User user) {
        if (user == null)
            return;
        mPortrait.setup(Glide.with(this), user);
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
        mFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));
        mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));

        hideLoading();

    }


    @Override
    public void allowSayHello(boolean isAllow) {
        mSayHello.setVisibility(isAllow ? View.VISIBLE : View.GONE);
        mBtLogout.setVisibility(!isAllow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        mIsFollow = isFollow;
        changeFollowStatus();
    }

    @Override
    public String getUserId() {
        return userId;
    }


    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }
}
