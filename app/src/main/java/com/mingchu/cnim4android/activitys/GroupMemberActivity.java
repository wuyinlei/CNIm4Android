package com.mingchu.cnim4android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mingchu.cnim4android.R;
import com.mingchu.common.app.BaseSwipeBackActivity;
import com.mingchu.common.app.PresenterToolbarActivity;
import com.mingchu.common.app.ToolbarActivity;
import com.mingchu.common.widget.custom.PortraitView;
import com.mingchu.common.widget.recycler.RecyclerAdapter;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.mingchu.factory.presenter.group.GroupMembersContract;
import com.mingchu.factory.presenter.group.GroupMembersPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberActivity extends PresenterToolbarActivity<GroupMembersContract.Presenter> implements GroupMembersContract.View{

    private static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    private static final String KEY_GROUP_ADMIN = "KEY_GROUP_ADMIN";


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MyAdapter mMyAdapter;

    private String groupId;
    private boolean isAdmin;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void initView() {
        super.initView();

        setTitle(R.string.title_member_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);

    }

    public static void show(Context context,String groupId){

       show(context,groupId,false);
    }

    public static void showAdmin(Context context, String groupId){
      show(context,groupId,true);
    }

    public static void show(Context context, String groupId, boolean isAdmin){
        if (TextUtils.isEmpty(groupId))
            return;

        Intent intent = new Intent(context,GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID,groupId);
        intent.putExtra(KEY_GROUP_ADMIN,isAdmin);
        context.startActivity(intent);
    }

    @Override
    protected GroupMembersContract.Presenter initPresenter() {
        return new GroupMembersPresenter(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.refresh();

    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerViewAadpter() {
        return mMyAdapter;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        String groupId = bundle.getString(KEY_GROUP_ID);
        this.groupId = groupId;
        this.isAdmin = bundle.getBoolean(KEY_GROUP_ADMIN);
        return !TextUtils.isEmpty(groupId);
    }

    @Override
    public void onAdapterDataChanged() {
            hideLoading();
    }

    @Override
    public void scrollRecyclerToPosition(int position) {

    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    class MyAdapter extends RecyclerAdapter<MemberUserModel>{

        @Override
        protected int getItemViewType(int position, MemberUserModel data) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
            return new GroupMemberActivity.ViewHolder(root);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel>{

        @BindView(R.id.iv_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mTvName;

        @BindView(R.id.cb_select)
        CheckBox mCheckBox;

        @OnClick(R.id.iv_portrait)
        void onPortraitClick(){
            PersonalActivity.show(GroupMemberActivity.this,mData.getUserId());
        }

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }

        @Override
        protected void onBind(MemberUserModel userModel) {
            mTvName.setText(userModel.getName());
            mPortraitView.setup(Glide.with(GroupMemberActivity.this),userModel.getPortrait());
        }
    }

}
