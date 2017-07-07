package com.mingchu.cnim4android.fragment.message;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.cnim4android.activitys.PersonalActivity;
import com.mingchu.common.factory.presenter.BaseContract;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.GroupMember;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.mingchu.factory.persistence.Account;
import com.mingchu.factory.presenter.message.ChatContract;
import com.mingchu.factory.presenter.message.ChatGroupPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * @author wuyinlei
 * @function 群聊天
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    @BindView(R.id.lay_members)
    LinearLayout mLayMembers;

    @BindView(R.id.im_header)
    ImageView mHeader;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingtoolbarlayout;

    @BindView(R.id.txt_member_more)
    TextView mMemberMore;


    private MenuItem mInfoMenuItem;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);


    }

    @Override
    public void onInit(Group group) {

        mToolbar.setTitle(group.getName());

        LayoutInflater inflater = LayoutInflater.from(getContext());
        List<MemberUserModel> models = group.getLatelyGroupMembers();
        for (final MemberUserModel model : models) {
            ImageView p = (ImageView) inflater.inflate(R.layout.lay_chat_group_portrait, mLayMembers, false);
            mLayMembers.addView(p, 0);
            Glide.with(this)
                    .load(model.portrait)
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .into(p);
            p.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonalActivity.show(getContext(), model.userId);
                }
            });
        }

        final long memberCount = group.getGroupMemberCount();
        mMemberMore.setText(String.format("+%s", memberCount - models.size()));
        mMemberMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (memberCount > models.size()) {
            mMemberMore.setText(String.format("+%s", memberCount - models.size()));
            mMemberMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            mMemberMore.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(group.getPicture())
                .centerCrop()
                .placeholder(R.mipmap.default_bannar_group)
                //.bitmapTransform(new Common.BlurTransformation(getContext()))
                .into(mHeader);


        if (Account.getUserId().equalsIgnoreCase(group.getOwner().getId())) {
            mToolbar.inflateMenu(R.menu.chat_group);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_add) {

                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this,mReceiverId);
    }

    @Override
    public void showAdminOptions(boolean isAdmin) {

    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> memberUserModels, long moreCount) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

}
